/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.plugin;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.BaseService;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * warn级别日志分析器
 * @author lianzt
 */
@Component
public class WarnLogAnalyze implements AnalyzePlugin {

    private final Logger log = LoggerFactory.getLogger(WarnLogAnalyze.class);
    private final Pattern p = Pattern.compile("\\( (\\d{6}) \\)");
    private final Set<String> MISS_CLASS_SET = new HashSet<String>();     //忽略该日志

    @PostConstruct
    public void init() {
        MISS_CLASS_SET.add("org.springframework.web.servlet.handler.SimpleMappingExceptionResolver");
        MISS_CLASS_SET.add("com.soa.porxy.ServicePorxy");
    }

    @Override
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        if (!event.getLevel().equals(Level.WARN) || MISS_CLASS_SET.contains(event.getLoggerName())) {
            return;
        }
        String msg = event.getMessage();
        log.debug("接收到WARN日志：{}", msg);
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putStringValue("server", server);
        in.putDateValue("save_time", new Date(event.getTimeStamp()));
        in.putStringValue("class_name", event.getLoggerName());
        in.putStringValue("msg", msg);
        Matcher m = p.matcher(msg);
        if (m.find()) {
            in.putStringValue("err_code", m.group(1));
            in.putStringValue("err_msg", msg.substring(m.end() + 1));
        }
        BaseService.runService(in, "P31008");
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
