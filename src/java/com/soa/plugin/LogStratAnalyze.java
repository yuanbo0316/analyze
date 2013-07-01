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
 *日志开始结束分析
 * @author chen
 */
@Component
public class LogStratAnalyze implements AnalyzePlugin {
    private final Logger log = LoggerFactory.getLogger(LogStratAnalyze.class);
    private final static String Log_Mark = "['log-start-log']" ;

    @Override
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        if(!Log_Mark.equals(logMark) || json==null){
            return;
        }
        String msg = event.getMessage();
        log.debug("接收的{}标记的数据{}",logMark,json);
        
        //opt：stop--0;start--1
        String opt;
        if(json.getStringValue("opt").equals("stop")){
            opt = "0";
        }else{
            opt = "1";
        }
        
        
        AbstractCommonData in = DataConvertFactory.getInstance();       
        in.putStringValue("opt", opt);
        in.putStringValue("server", server);
        in.putDateValue("exec_time", new Date());
        BaseService.runService(in, "P33001");
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
