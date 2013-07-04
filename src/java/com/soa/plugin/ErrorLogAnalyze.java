/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.plugin;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.BaseService;
import com.soa.websocket.ErrorMsgServlet;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * error级别日志分析器
 * @author lianzt
 */
@Component
public class ErrorLogAnalyze implements AnalyzePlugin {

    private final Logger log = LoggerFactory.getLogger(ErrorLogAnalyze.class);

    @Override
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        if (!event.getLevel().equals(Level.ERROR)) {
            return;
        }
        ErrorMsgServlet.msgToAll(event.getMessage());           //通知所有客户端
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putDateValue("save_time", new Date(event.getTimeStamp()));
        in.putStringValue("server", server);
        in.putStringValue("class_name", event.getLoggerName());
        in.putStringValue("msg", event.getMessage());
        if (event.getThrowableProxy() != null) {
            in.putStringValue("err_stack", ThrowableProxyUtil.asString(event.getThrowableProxy()));
        }
        BaseService.runService(in, "P31009");
    }

    @Override
    public AbstractCommonData getFlash() {
        return null;
    }

    @Override
    public void clear() {
    }

    @Override
    public void saveFlash() {
    }
}
