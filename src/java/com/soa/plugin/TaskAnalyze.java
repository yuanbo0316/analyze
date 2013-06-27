/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.plugin;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.BaseService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author chen
 */
@Component
public class TaskAnalyze implements AnalyzePlugin {
    Logger log = LoggerFactory.getLogger(TaskAnalyze.class);
    private final String TASK_MARK = "['log-task-time']";

    @Override
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        log.debug("====================={}",logMark);
        if(!TASK_MARK.equals(logMark) || json==null){
            return;
        }
        
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putAll(json);
        in.putDateValue("save_time", new Date());
        in.putStringValue("server", server);
        in.putIntValue("run_time", json.getIntValue("_runTime"));
        BaseService.runService(in, "P33002");
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
