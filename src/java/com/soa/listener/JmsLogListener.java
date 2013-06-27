package com.soa.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.exception.InstanceDataException;
import com.soa.exception.GlobalException;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.BaseService;
import com.soa.service.impl.UtilService;
import com.soa.util.SystemUtil;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 在spring配置文件中配置，可以有多个实例
 * @author lianzt
 */
public class JmsLogListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(JmsLogListener.class);
    /**
     * 监控的系统名
     */
    private String server;
    /**
     * 插件需要的执行的分析器
     */
    private List<AnalyzePlugin> analyzePlugins = null;
    private UtilService utilServiceImpl;

    @Override
    public void onMessage(Message message) {
        ObjectMessage om = (ObjectMessage) message;
        try {
            ILoggingEvent le = (ILoggingEvent) om.getObject();
            if (Level.DEBUG.equals(le.getLevel())) {
                //debug 日志不分析
                return;
            }
            String logMsg = le.getMessage();
            log.debug("接收到日志 {} : {}", server, logMsg);
            String logMark = null;
            AbstractCommonData logData = null;
            if (le.getLevel().equals(Level.INFO)
                    && (logMsg.startsWith("分析日志") || logMsg.startsWith("['log"))) {
                int markBegin = logMsg.indexOf("['log");
                int markEnd = logMsg.indexOf("\t", markBegin);
                logMark = logMsg.substring(markBegin, markEnd);
                String json = logMsg.substring(markEnd + 1);
                try {
                    logData = DataConvertFactory.getInstanceByJson(json);
                    log.debug("收到可分析的日志 {}:{}={}", new Object[]{server, logMark, logData});
                } catch (InstanceDataException e) {
                    log.warn("出现异常的json格式：{}", json);
                    log.warn("出现异常的json格式:", e);
                    AbstractCommonData in = DataConvertFactory.getInstance();
                    in.putStringValue("server", server);
                    in.putStringValue("json", json);
                    try {
                        BaseService.runService(in, "P31007");
                    } catch (Exception ex) {
                        log.error("保存异常json时出错：", ex);
                    }
                }
            }
            if (analyzePlugins == null) {
                analyzePlugins = SystemUtil.serverAnalyze.get(server);
                if (analyzePlugins == null) {
                    log.debug("未启动完成，丢弃报文！");
                    return;
                }
            }
            for (AnalyzePlugin ap : analyzePlugins) {
                try {
                    ap.analyze(logData, le, logMark, server);
                } catch (Exception e) {
                    log.warn("分析器" + ap.getClass().getSimpleName() + "出现异常：", e);
                    if (e instanceof GlobalException) {
                        GlobalException ge = (GlobalException) e;
                        if (ge.getErrorCode() > 900000) {
                            utilServiceImpl.saveError(ge);
                        }
                    } else {
                        utilServiceImpl.saveError(e);
                    }
                }
            }
//            System.out.println(String.format("%-5s %s [%s] %s ---> %s",
//                    le.getLevel(), new Date(le.getTimeStamp()), le.getThreadName(),
//                    le.getLoggerName(), le.getMessage()));
//            if (le.getThrowableProxy() != null) {
//                System.out.println("-->" + ThrowableProxyUtil.asString(le.getThrowableProxy()));
//            }
        } catch (JMSException e) {
            log.warn("接收到异常jms报文：", e);
        }
    }

    public JmsLogListener() {
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public JmsLogListener(String server, UtilService utilServiceImpl) {
        this.server = server;
        this.utilServiceImpl = utilServiceImpl;
    }

    public UtilService getUtilServiceImpl() {
        return utilServiceImpl;
    }

    public void setUtilServiceImpl(UtilService utilServiceImpl) {
        this.utilServiceImpl = utilServiceImpl;
    }
}