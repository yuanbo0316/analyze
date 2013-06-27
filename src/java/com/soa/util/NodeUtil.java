/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.util;

import com.lianzt.commondata.AbstractCommonData;
import com.lianzt.util.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 节点判断工具，用于判断发出请求的终端类型，由 spring 容器管理，以单利格式运行
 * @author lianzt
 */
@Component
public class NodeUtil {

    /**
     * 终端类型
     */
    public final String OTHER = "0";
    public final String TIMER = "1";
    public final String PC_BROWSER = "2";
    public final String MOBILE_BROWSER = "3";
    public final String APP_JS_CLIENT = "4";
    public final String ANDROID_CLIENT = "5";
    public final String IPHONE_CLIENT = "6";
    public final String ANDROID_PAD_CLIENT = "7";
    public final String IPAD_CLIENT = "8";
    public final String WEBSERVICE_INTER = "9";

    /**
     * 判断请求的发出节点
     * @param json
     * @return
     */
    public String determineServiceNode(AbstractCommonData json) {
        AbstractCommonData head = json.getDataValue("head");
        if (StringUtil.notNull(head.getStringValue("mac"))
                || StringUtil.notNull(json.getStringValue("csid"))) {
            return APP_JS_CLIENT;
        } else if ("_timer".equals(head.getStringValue("username"))) {
            return TIMER;
        } else if ("_inter".equals(head.getStringValue("username"))) {
            return WEBSERVICE_INTER;
        } else {
            return PC_BROWSER;
        }
    }
}
