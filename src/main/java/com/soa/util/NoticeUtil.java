/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.util;

import com.lianzt.util.HttpConnect;
import com.soa.util.SystemUtil;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常信息提示 websocket 接口，url：/analyze/ErrorMsgServlet
 * @author lianzt
 */
public class NoticeUtil {

    private static final Logger log = LoggerFactory.getLogger(NoticeUtil.class);

    /**
     * 向所有连接发送数据
     * @param msg
     */
    public static void msgToAll(String msg) {
        HttpConnect hc = null;
        try {
            hc = new HttpConnect(SystemUtil.getSysConfig("node_url"));
            hc.addParameter("msg", msg);
            hc.sendMsg();
            log.info("在线人数：{}", hc.receMsg().trim());
        } catch (IOException e) {
            log.error("发送实时消息异常：", e);
        } finally {
            if (hc != null) {
                hc.closeStream();
            }
        }
    }
}
