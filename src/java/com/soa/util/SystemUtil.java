package com.soa.util;

import com.soa.bean.StServiceBean;
import com.soa.bean.StTableParamet;
import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.factory.AESFactory;
import com.lianzt.util.DateUtil;
import com.lianzt.util.StringUtil;
import com.soa.exception.GlobalException;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.impl.UtilService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import sun.misc.BASE64Decoder;

/**
 * 系统参数与系统方法
 * @author lianzt
 */
public class SystemUtil {

    public static final Logger log = LoggerFactory.getLogger(SystemUtil.class);
    public static String serverName = "SOAFrame";       //服务名
    public static int timeout = 10000;             //超时时间 (ms)
    public static int serialCache = 1000;         //流水号
    public static String charset = "utf-8";       //字符格式
    public static String node = "01";           //集群部署时的节点名
    public static boolean sessionCache = true;   //session的缓存方式,true:session, false:内存数据库
    public static String loginRemark = "loginname";          //登录状态的校验标记
    public static String serverDesc = "郑州交通违法管理系统";       //服务中文名称
    //系统中的所有自定义异常
    public static Map<Integer, String> exMap = null;
    public static UtilService utilServiceImpl;
    public static Map<String, List<StTableParamet>> tableMap;
    public static Map<String, StServiceBean> serviceMap;
    public static WebApplicationContext wac;
    public static Set<String> msgCache = new HashSet<String>();
    public static Properties systemConfig = new Properties();               //system.properties配置文件
    public static Properties pageServiceProperties = new Properties();      //pageService 的 key-value 键值对
    public static Map<String, String> serverSql = new HashMap<String, String>();        //其它服务的sql语句
    public static Map<String, List<AnalyzePlugin>> serverAnalyze = new HashMap<String, List<AnalyzePlugin>>();        //服务对应的分析器
    public static Map<String, AbstractCommonData> sqlMap = new HashMap<String, AbstractCommonData>();       //sql语句

    /**
     * 保存文件
     * @param file
     * @param path
     * @param fileFileName
     * @throws IOException
     */
    public static void saveFile(File file, String path, String fileFileName)
            throws IOException {
        InputStream is = null;
        FileOutputStream os = null;
        try {
            if (StringUtil.isNull(fileFileName)) {
                throw new FileNotFoundException();
            }
            if (StringUtil.isNull(path)) {
                path = "";
            }
            os = new FileOutputStream(new File(path, fileFileName));
            is = new FileInputStream(file);
            int length = 0;
            byte[] buf = new byte[50 * 1024];
            while ((length = is.read(buf)) > 0) {
                os.write(buf, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 保存文件
     * @param bs 文件字节数组
     * @param path 路径
     * @param fileFileName 文件名
     * @throws IOException
     */
    public static void saveFile(byte[] bs, String path, String fileFileName)
            throws IOException {
        FileOutputStream os = null;
        try {
            if (StringUtil.isNull(fileFileName)) {
                throw new FileNotFoundException();
            }
            if (StringUtil.isNull(path)) {
                path = "";
            }
            os = new FileOutputStream(new File(path, fileFileName));
            os.write(bs);
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取流水号
     * 前置的流水号只在校验出现异常，且报文的流水号为空时使用
     * @return 流水号
     */
    public static synchronized String getSerialNum() throws GlobalException {
        try {
            if (serialCache >= 9999) {
                serialCache = 1000;
            } else {
                serialCache++;
            }
            return "A" + DateUtil.detaledLshFormat(new Date()) + serialCache;
        } catch (Exception e) {
            log.error("流水号更新异常 : " + e);
            throw new GlobalException(999995);     //生成流水号异常
        }
    }

    /**
     * 获取异常对象
     * @param code 异常码
     * @return
     */
    public static String getExByCode(Integer code) {
        String ge = exMap.get(code);
        if (ge != null) {
            return ge;
        } else {
            return "未知的异常代码 : " + code;
        }
    }

    /**
     * 生成错误包
     * @param ge
     * @return
     */
    public static AbstractCommonData creatErrorCommonData(GlobalException ge) {
        AbstractCommonData acd = DataConvertFactory.getInstance();
        AbstractCommonData head = acd.getDataValue("head");
        head.putStringValue("response_code", ge.getErrorCode() + "");
        head.putStringValue("response_desc", ge.getErrorMsg());
        return acd;
    }

    /**
     * 生成错误包
     * @param ge
     * @return
     */
    public static void creatErrorCommonData(GlobalException ge,
            AbstractCommonData head) {
        head.putStringValue("response_code", ge.getErrorCode() + "");
        head.putStringValue("response_desc", ge.getErrorMsg());
    }

    /**
     * 获取服务接口
     * @param serviceCode
     * @return
     * @throws GlobalException
     */
    public static StServiceBean getServiceByCode(String serviceCode) throws
            GlobalException {
        StServiceBean service = serviceMap.get(serviceCode);
        if (service == null) {
            throw new GlobalException(999996, serviceCode);     //服务码不存在
        }
        return service;
    }

    /**
     * 获取sql语句
     * @param key
     * @return
     */
    public static String getSql(String key) {
        if (StringUtil.isNull(key)) {
            return null;
        }
        AbstractCommonData sql = sqlMap.get(key);
        if (sql == null || sql.isEmpty()) {
            log.warn("找不到sql语句：" + key);
            return null;
        }
        return sql.getStringValue("sql");
    }

    /**
     * 生成 SQL语句 的参数
     * @param sql
     * @param args
     * @return
     */
    public static Object[] getSqlArgs(String sql, AbstractCommonData args) {
        AbstractCommonData sqlData = sqlMap.get(sql);
        if (sql == null || sql.isEmpty()) {
            return null;
        }
        log.debug("sql-name : {} \n sql-data : {}", sql, sqlData);
        List<String> keys = sqlData.getArrayValue("key");
        List<String> types = sqlData.getArrayValue("type");
        Object[] os = new Object[keys.size()];
        String type = null;
        for (int i = 0; i < keys.size(); i++) {
            type = types.get(i);
            if ("string".equalsIgnoreCase(type)) {
                os[i] = args.getStringValue(keys.get(i));
            } else if ("date".equalsIgnoreCase(type)) {
                os[i] = args.getDateValue(keys.get(i));
            } else if ("int".equalsIgnoreCase(type)) {
                os[i] = args.getIntValue(keys.get(i));
            } else if ("double".equalsIgnoreCase(type)) {
                os[i] = args.getDoubleValue(keys.get(i));
            } else if ("long".equalsIgnoreCase(type)) {
                os[i] = args.getLongValue(keys.get(i));
            } else {
                os[i] = args.getStringValue(keys.get(i));
            }
        }
        return os;
    }

    /**
     * 获取列表中某一个值对应的描述
     * @param col 列表名
     * @param value 值
     * @return 值的描述
     */
    public static String getColValueDesc(String col, String value) {
        if (StringUtil.isNull(col) || StringUtil.isNull(value)) {
            return null;
        }
        List<StTableParamet> list = tableMap.get(col);
        if (list.isEmpty()) {
            return null;
        }
        for (StTableParamet p : list) {
            if (p.getColValue().equals(value)) {
                return p.getValueDesc();
            }
        }
        return null;
    }

    /**
     * 获取system.properties文件的配置项
     * @param key
     * @return
     */
    public static String getSysConfig(String key) {
        return systemConfig.getProperty(key, null);
    }

    /**
     * 加密 + 转码， 可用在url中
     * @param str
     * @return
     * @throws Exception
     */
    public static String haxEncode(String str) throws Exception {
        str = AESFactory.encryptString(str);
        str = URLEncoder.encode(str, charset);
        return str;
    }

    /**
     * 转码 + 解密， 可用在url中
     * @param str
     * @return
     * @throws Exception
     */
    public static String haxDecode(String str) throws Exception {
        str = URLDecoder.decode(str, charset);
        str = AESFactory.decryptString(str);
        return str;
    }
}
