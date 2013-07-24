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
public class PicRequestAnalyze implements AnalyzePlugin {

    Logger log = LoggerFactory.getLogger(PicRequestAnalyze.class);
    private final String PIC_REQUEST_MARK = "['log-request-time']";

    @Override
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        if (!PIC_REQUEST_MARK.equals(logMark) || json == null) {
            return;
        }
        AbstractCommonData head = json.getDataValue("head");
        String url = head.getStringValue("_url");
        if (url.indexOf("/jtgzfw/client/_pic/") == -1) {
            return;
        }
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putDataValue("json", json);
        in.putDateValue("req_time", new Date(event.getTimeStamp()));
        in.putStringValue("server", server);
        BaseService.runService(in, "P33004");
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
