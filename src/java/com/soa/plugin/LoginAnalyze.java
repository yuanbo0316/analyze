/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.plugin;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.commondata.DataConvertFactory;
import com.lianzt.util.StringUtil;
import com.soa.plugin.impl.AnalyzePlugin;
import com.soa.service.BaseService;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户登录情况分析器
 * @author lianzt
 */
@Component
public class LoginAnalyze implements AnalyzePlugin {

    private final Logger log = LoggerFactory.getLogger(LoginAnalyze.class);
    public static final String SERVICE_MARK = "['log-service-time']";
    private final Set<String> loginServiceSet = new HashSet<String>();

    @PostConstruct
    public void init() {
        loginServiceSet.add("S23010");      //登录
        loginServiceSet.add("S23002");      //登录

        loginServiceSet.add("S24001");      //自动登录
        loginServiceSet.add("S21005");      //注销

        loginServiceSet.add("P11000");      //登录
    }

    @Override
    public void analyze(AbstractCommonData json, ILoggingEvent event, String logMark, String server) {
        if (json == null || !SERVICE_MARK.equals(logMark)) {
            return;
        }
        String serviceCode = json.getDataValue("head").getStringValue("service_code");
        if (!loginServiceSet.contains(serviceCode)) {
            return;
        }
        String opt = "";
        if ("S21005".equals(serviceCode)) {
            opt = "2";      //注销
        } else if ("S24001".equals(serviceCode)) {
            opt = "4";      //客户端超时自动登录
        } else if (StringUtil.isNull(json.getDataValue("head").getStringValue("response_code"))) {
            opt = "1";      //登录
        } else {
            opt = "5";      //登录失败
        }
        //当操作是注销，而且用户名为空时，不保存登录情况
        if ("2".equals(opt)
                && StringUtil.isNull(json.getDataValue("head").getStringValue("username"))
                && StringUtil.isNull(json.getDataValue("ipt_session_id").getStringValue("username"))) {
            return;
        }

        //计算完成后开始保存请求
        AbstractCommonData in = DataConvertFactory.getInstance();
        in.putStringValue("server", server);
        in.putStringValue("opt", opt);
        in.putDataValue("json", json);
        BaseService.runService(in, "P31005");     //保存服务
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
