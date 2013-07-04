/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.plugin;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.util.StringUtil;
import com.soa.exception.GlobalException;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.BaseService;
import com.soa.util.SystemUtil;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 服务运行情况分析
 * @author lianzt
 */
@Component
public class ServiceRunAnalyze implements AnalyzePlugin {

    private final Logger log = LoggerFactory.getLogger(ServiceRunAnalyze.class);
    private final AbstractCommonData flash = DataConvertFactory.getInstanceEmpty();
    public static final String SERVICE_MARK = "['log-service-time']";

    @Override
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        if (json == null || !SERVICE_MARK.equals(logMark)) {
            //不是我的菜
            return;
        }
        log.debug("收到的service: mark{}, json {}", logMark, json);
        //先统计快照，然后保存报文，在保存时判断是否超时
        String serviceCode = json.getDataValue("head").getStringValue("service_code");
        if (StringUtil.isNull(serviceCode)) {
            throw new GlobalException(310002);     //没有找到service_code字段
        }
        serviceCode = server + "." + serviceCode;
        int runTime = json.getIntValue("_runTime");
        synchronized (flash) {      //对快照进行更新时，需要加锁
            AbstractCommonData res = flash.getDataValue(serviceCode);       //历史数据
            if (res == null) {
                res = DataConvertFactory.getInstanceEmpty();
                res.putIntValue("min", runTime);
                res.putIntValue("max", runTime);
                res.putIntValue("avg", runTime);
                res.putIntValue("times", 1);
                flash.putDataValue(serviceCode, res);
            } else {
                res.putIntValue("min", Math.max(runTime, res.getIntValue("min")));
                res.putIntValue("max", Math.max(runTime, res.getIntValue("max")));
                res.putIntValue("avg", (runTime + res.getIntValue("avg")) >> 1);        //右移一位，代替除2
                res.putIntValue("times", res.getIntValue("times") + 1);
            }
        }

        //计算完成后开始保存请求
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putDateValue("exec_time", new Date(event.getTimeStamp()));
        in.putStringValue("server", server);
        in.putDataValue("json", json);
        BaseService.runService(in, "P31003");     //保存服务


    }

    @Override
    public AbstractCommonData getFlash() {
        return (AbstractCommonData) flash.clone();
    }

    @Override
    public void clear() {
        flash.clear();
    }

    @Override
    public void saveFlash() {
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putDataValue("flash", flash);
        log.info("开始保存快照 {} 条数据。", flash.size());
        synchronized (flash) {          //保存时加锁
            BaseService.runService(in, "P31004");        //保存
            clear();        //保存后
        }
    }
}
