package com.soa.listener;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.factory.AESFactory;
import com.soa.interceptor.ReqTimeInterceptor;
import com.soa.service.impl.UtilService;
import com.lianzt.util.DateUtil;
import com.lianzt.util.LogUtil;
import com.lianzt.util.StringUtil;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.task.BaseTask;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import com.soa.util.SystemUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 加载所有启动项
 *
 * @author lianzt
 */
@Component
public class ReadGlobalArgsListener implements ServletContextListener {

    private final Logger log = LoggerFactory.getLogger(ReadGlobalArgsListener.class);
    private Timer timer = null;
    private static String path = null;
    private static String sqlProPath = null;
    private final long DAY_SECOND = 86400000;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 加载配置信息
        log.info("开始加载配置文件 ...");
        AESFactory.setKey("lian.zuotai.1030".getBytes());
        ReadGlobalArgsListener.path = sce.getServletContext().getRealPath("/WEB-INF") + File.separator;
        readSysPro();       //读system.properties文件
        //spring 容器
        SystemUtil.wac = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        SystemUtil.utilServiceImpl = (UtilService) SystemUtil.wac.getBean("utilServiceImpl");

        readErrMsg();       // 加载所有异常信息

        readTableParam();   //加载 st_table_paramet

        readPageService();      //加载 pageService

        readSqlFile();          //加载 sql 语句

        readServerXML();        //加载 server.xml

        readTimer();        //加载定时任务

        log.info("启动项加载完成。。。");
        LogUtil.analysisJsonLog("{\"opt\":\"start\"}", "start-log");
    }

    /**
     * 读 System.properties 文件
     */
    public void readSysPro() {
        FileInputStream fis = null;
        try {
            log.info("配置文件 path : " + ReadGlobalArgsListener.path);
            fis = new FileInputStream(ReadGlobalArgsListener.path + "system.properties");
            SystemUtil.systemConfig.load(fis);
            Properties p = SystemUtil.systemConfig;
//            SystemUtil.version = p.getProperty("version");
            SystemUtil.serverName = p.getProperty("server_name");
            SystemUtil.serverDesc = p.getProperty("server_desc");
            SystemUtil.timeout = Integer.parseInt(p.getProperty("timeout"));
            SystemUtil.charset = p.getProperty("charset");
            SystemUtil.sessionCache = Boolean.parseBoolean(p.getProperty("sesion_cache"));
            ReqTimeInterceptor.warnTime = Integer.parseInt(p.getProperty("request_warn_time"));
            //创建文件上传目录，如果目录已存在，什么也不做。
            SystemUtil.loginRemark = p.getProperty("login_remark");
        } catch (FileNotFoundException e) {
            log.warn("/WEB-INF/system.properties文件不存在！ 使用默认配置.");
        } catch (Exception e) {
            log.warn("配置文件读取异常 ： " + e + "\n使用默认配置.");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
            if (log.isInfoEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n配置文件加载完成:");
//                sb.append("\n\t版本号 ：");
//                sb.append(SystemUtil.version);
                sb.append("\n\t服务名 ：");
                sb.append(SystemUtil.serverName);
                sb.append("\n\t超时时间（毫秒） ：");
                sb.append(SystemUtil.timeout);
                sb.append("\n\t字符集：");
                sb.append(SystemUtil.charset);
                sb.append("\n\t请求耗时预警时间，单位 ms：");
                sb.append(ReqTimeInterceptor.warnTime);
                sb.append("\n\t是否使用session做缓存：");
                sb.append(SystemUtil.sessionCache);
                sb.append("\n\t登录状态的校验标记：");
                sb.append(SystemUtil.loginRemark);
                log.info(sb.toString());
            }
        }
    }

    /**
     * 读 st_error_msg 表
     */
    public void readErrMsg() {
        log.info("开始加载异常代码和异常信息。。。");
        try {
            SystemUtil.exMap = SystemUtil.utilServiceImpl.readErrMsg();
        } catch (DataAccessException e) {
            log.error("加载异常代码和异常信息出现错误 ： ", e);
        }

        if (SystemUtil.exMap != null && !SystemUtil.exMap.isEmpty()) {
            log.info("读取数据库成功，共 " + SystemUtil.exMap.size() + " 条异常信息");
        } else {
            log.warn("读取数据异常，异常信息和异常代码加载失败。。。");
        }
    }

    /**
     * 读 st_table_paramet 表
     */
    public void readTableParam() {
        log.info("开始加载下拉列表选项。。。");
        SystemUtil.tableMap = SystemUtil.utilServiceImpl.readTablePara();
        if (SystemUtil.tableMap == null || SystemUtil.tableMap.isEmpty()) {
            log.warn("读取下拉列表选项为空，需要检查数据表是否有数据！");
        }
        log.info("加载下拉列表选项完成，共加载 " + SystemUtil.tableMap.size() + " 项");

        //加载 service bean
        log.info("开始加载service bean。。。");
        SystemUtil.serviceMap = SystemUtil.utilServiceImpl.readServiceBean();
        if (SystemUtil.serviceMap == null || SystemUtil.serviceMap.isEmpty()) {
            log.warn("读取service bean为空，需要检查数据表是否有数据！");
        }
        log.info("加载service bean完成，共加载 " + SystemUtil.serviceMap.size() + " 项");
    }

    /**
     * 加载定时器
     */
    public void readTimer() {
        if (timer != null) {
            log.info("销毁定时器！");
            timer.cancel();
        }
        //读timer.xml配置文件
        File timerXml = new File(path + "timer.xml");
        SAXReader reader = new SAXReader();
        log.info("打开配置文件：timer.xml");
        try {
            Document document = reader.read(timerXml);
            Element root = document.getRootElement();
            Iterator<Element> rootIterable = root.elementIterator("timer");
            if (!rootIterable.hasNext()) {
                return;
            }
            List<BaseTask> tasks = new LinkedList<BaseTask>();
            BaseTask task = null;
            //读 timer.xml 配置文件
            while (rootIterable.hasNext()) {
                Element timerElement = rootIterable.next();
                task = (BaseTask) SystemUtil.wac.getBean("baseTask");
                task.setDesc(timerElement.attributeValue("desc"));
                log.info("开始读取定时器：{}", task.getDesc());
                task.setRunTime(timerElement.elementTextTrim("run-time"));
                task.setCycle(timerElement.elementTextTrim("cycle"));
                task.setServiceCode(timerElement.elementTextTrim("service-code"));
                task.setArgs(timerElement.elementTextTrim("args"));
                task.setDelay(timerElement.elementTextTrim("delay"));
                tasks.add(task);
            }
            //初始化定时器
            timer = new Timer();
            Date runTime = null;
            int successTask = 0;
            for (BaseTask bt : tasks) {
                log.info("加载定时器：{}", bt.toString());
                if (bt.getRunTime() != null && !Pattern.matches("\\d{1,2}?:\\d{1,2}?|^", bt.getRunTime())) {
                    log.warn("定时器 {} ，的运行时间 {} 格式有误。", bt.getDesc(), bt.getRunTime());
                } else if (bt.getCycle() != null && !Pattern.matches("\\d+|^", bt.getCycle())) {
                    log.warn("定时器 {} 的运行周期 {} 格式有误。", bt.getDesc(), bt.getCycle());
                } else if (!SystemUtil.serviceMap.containsKey(bt.getServiceCode())) {
                    log.warn("定时器 {} 需要执行的服务 {} 不存在！", bt.getDesc(), bt.getServiceCode());
                } else {
                    long cycle = 0;
                    if (StringUtil.isNull(bt.getCycle())) {
                        cycle = DAY_SECOND;
                    } else {
                        cycle = 1000L * Integer.parseInt(bt.getCycle());
                    }
                    if (StringUtil.isNull(bt.getRunTime())) {
                        if (StringUtil.isNull(bt.getDelay())) {
                            timer.schedule(bt, 10000, cycle);
                        } else {
                            timer.schedule(bt, Integer.valueOf(bt.getDelay()) * 1000, cycle);
                        }
                    } else {
                        String[] ts = bt.getRunTime().split(":");
                        runTime = new Date();
                        runTime = DateUtil.setHour(runTime, Integer.parseInt(ts[0]));
                        runTime = DateUtil.setMinute(runTime, Integer.parseInt(ts[1]));
                        runTime = DateUtil.setSecond(runTime, 0);
                        if (runTime.getTime() < new Date().getTime()) {
                            runTime = new Date(runTime.getTime() + DAY_SECOND);
                        }
                        timer.schedule(bt, runTime, cycle);
                    }
                    successTask++;
                }
            }
            log.info("定时器加载完成，共 {} 项，成功 {} 项。", tasks.size(), successTask);
        } catch (DocumentException e) {
            log.error("定时任务加载失败！", e);
        }
    }

    /**
     * 读取pageService.properties文件
     */
    public void readPageService() {
        log.info("开始加载page-service。。。");
        log.info("page-service 配置文件 path : " + ReadGlobalArgsListener.path + "pageService.properties");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(ReadGlobalArgsListener.path + "pageService.properties");
            SystemUtil.pageServiceProperties.load(fis);
        } catch (FileNotFoundException e) {
            log.error("/WEB-INF/pageService.properties文件不存在！");
        } catch (Exception e) {
            log.error("sql配置文件读取异常 ： " + e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    log.error("pageService.properties error : " + ioe);
                }
            }
        }
        log.info("共加载 " + SystemUtil.pageServiceProperties.size() + " 条。");
    }

    /**
     * 读 sql 文件
     */
    public void readSqlFile() {
        log.info("开始加载sql语句。。");
        ReadGlobalArgsListener.sqlProPath = ReadGlobalArgsListener.path + "sql" + File.separator;
        log.info("sql语句配置文件 path : " + ReadGlobalArgsListener.sqlProPath);
        FileInputStream fis = null;
        File sqlPath = new File(ReadGlobalArgsListener.sqlProPath);
        if (sqlPath.isDirectory()) {
            //循环加载sql语句
            Properties sqlProperties = new Properties();
            File[] fs = sqlPath.listFiles();
            for (File f : fs) {
                try {
                    fis = new FileInputStream(f);
                    sqlProperties.load(fis);
                } catch (FileNotFoundException e) {
                    log.error("/WEB-INF/" + f + ".properties文件不存在！");
                } catch (Exception e) {
                    log.error("sql配置文件读取异常 ： " + e);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException ioe) {
                            log.error(f + ".properties error : " + ioe);
                        }
                    }
                }
            }
            Pattern p = Pattern.compile(":(\\w+)\\[(\\w*)\\]");     //匹配SQL语句中的关键字
            for (String sqlName : sqlProperties.stringPropertyNames()) {
                String sql = sqlProperties.getProperty(sqlName);
                AbstractCommonData sqlData = DataConvertFactory.getInstanceEmpty();
                if (sql.indexOf(":") == -1) {       //不包含:号，为原始sql
                    sqlData.putStringValue("sql", sql);
                    SystemUtil.sqlMap.put(sqlName, sqlData);
                    continue;
                }
                Matcher m = p.matcher(sql);
                StringBuffer sb = new StringBuffer();
                LinkedList<String> keys = new LinkedList<String>();
                LinkedList<String> types = new LinkedList<String>();
                while (m.find()) {
                    keys.add(m.group(1));
                    try {
                        types.add(StringUtil.changeNull(m.group(2), "string"));
                    } catch (IllegalStateException e) {
                        types.add("string");
                    }
                    m.appendReplacement(sb, "?");
                }
                m.appendTail(sb);
                sqlData.putStringValue("sql", sb.toString());
                sqlData.putArrayValue("key", keys);
                sqlData.putArrayValue("type", types);
                SystemUtil.sqlMap.put(sqlName, sqlData);
            }

            log.info("共加载SQL语句 " + SystemUtil.sqlMap.size() + " 条。");
        } else {
            log.warn(ReadGlobalArgsListener.sqlProPath + " 目录下没有找到SQL文件。。");
        }
    }

    /**
     * 加载监控系统的XML
     */
    public void readServerXML() {
        log.info("开始读取 server.xml 文件。");
        File serverXml = new File(path, "server.xml");

        SAXReader reader = new SAXReader();
        log.info("打开配置文件：timer.xml");
        try {
            Document document = reader.read(serverXml);
            Element root = document.getRootElement();
            Iterator<Element> rootIterable = root.elementIterator("server");
            if (!rootIterable.hasNext()) {
                return;
            }
            while (rootIterable.hasNext()) {
                Element serverElement = rootIterable.next();
                String name = serverElement.elementTextTrim("name");
                String sqlDir = serverElement.elementTextTrim("sql_dir");
                readServerSQL(name, sqlDir);        //读取被监控系统的sql语句，被加载的sql语句以server_name.sql_name命名
                //加载分析器
                List<AnalyzePlugin> analyzeList = new LinkedList<AnalyzePlugin>();
                SystemUtil.serverAnalyze.put(name, analyzeList);
                Iterator<Element> analyzeIterator = serverElement.elementIterator("analyze");
                while (analyzeIterator.hasNext()) {
                    String analyzeName = analyzeIterator.next().getTextTrim();
                    analyzeList.add((AnalyzePlugin) SystemUtil.wac.getBean(analyzeName));
                }
            }
            log.info("监控 {} 个服务", SystemUtil.serverAnalyze.size());
        } catch (DocumentException de) {
            log.warn("读取 server.xml 文件异常：" + de);
        }
    }

    /**
     * 读取被监控系统的sql语句，被加载的sql语句以server_name.sql_name命名
     * @param server
     * @param sqlDir
     */
    public void readServerSQL(String server, String sqlDir) {
        log.info("开始读取监控系统的SQL语句，server={}, sql_dir={}", server, sqlDir);

        FileInputStream fis = null;
        File sqlPath = new File(sqlDir);
        Properties sqlProperties = new Properties();
        if (sqlPath.isDirectory()) {
            //循环加载sql语句
            File[] fs = sqlPath.listFiles();
            for (File f : fs) {
                try {
                    fis = new FileInputStream(f);
                    sqlProperties.load(fis);
                } catch (FileNotFoundException e) {
                    log.error(f.getPath() + "文件不存在！");
                } catch (Exception e) {
                    log.error("sql配置文件读取异常 ： " + e);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException ioe) {
                            log.error(f.getPath() + ".properties error : " + ioe);
                        }
                    }
                }
            }
            Set<String> key = sqlProperties.stringPropertyNames();
            for (String k : key) {
                SystemUtil.serverSql.put(server + "." + k, sqlProperties.getProperty(k));
            }
            log.info("共加载SQL语句 " + SystemUtil.serverSql.size() + " 条。");
        } else {
            log.warn(sqlDir + " 目录下没有找到SQL文件。。");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (timer != null) {
            log.info("销毁定时器！");
            timer.cancel();
        }
        LogUtil.analysisJsonLog("{\"opt\":\"stop\"}", "start-log");
    }
}
