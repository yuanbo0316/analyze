/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.websocket;

import com.lianzt.util.HttpConnect;
import com.soa.util.SystemUtil;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常信息提示 websocket 接口，url：/analyze/ErrorMsgServlet
 * @author lianzt
 */
public class ErrorMsgServlet extends WebSocketServlet {

    private static final Logger log = LoggerFactory.getLogger(ErrorMsgServlet.class);
    public static final Set<MessageInbound> sockets = new HashSet<MessageInbound>();

    @Override
    protected StreamInbound createWebSocketInbound(String str, HttpServletRequest req) {
        HttpSession ses = req.getSession();
        MessageInbound mi = new ErrorMsgInbound();
        log.debug("timeout = {}", mi.getReadTimeout());
        return mi;
    }

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
//    public synchronized static void msgToAll(String msg) {
//        try {
//            for (MessageInbound mi : sockets) {
//                try {
//                    mi.getWsOutbound().writeTextMessage(CharBuffer.wrap(msg));
//                } catch (IOException e) {
//                    log.warn("发送 websocket 通知时出现异常：", e);
//                }
//            }
//        } catch (Exception e) {
//            log.error("set error : ", e);
//        }
//    }
}
