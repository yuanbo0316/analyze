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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * url请求分析器
 * @author lianzt
 */
@Component
public class RequestAnalyze implements AnalyzePlugin {

    private final Logger log = LoggerFactory.getLogger(RequestAnalyze.class);
    public static final String SERVICE_MARK = "['log-request-time']";

    @Override
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        if (json == null || !SERVICE_MARK.equals(logMark)) {
            return;
        }
        log.debug("收到的request: mark{}, json {}", logMark, json);
        //计算完成后开始保存请求
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putStringValue("server", server);
        in.putDataValue("json", json);
        BaseService.runService(in, "P31006");     //保存服务
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
