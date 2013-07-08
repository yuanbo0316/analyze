/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.soa.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lianzt
 */
public class ErrorMsgInbound extends MessageInbound {

    private final Logger log = LoggerFactory.getLogger(ErrorMsgInbound.class);

    @Override
    protected void onBinaryMessage(ByteBuffer bb) throws IOException {
    }

    @Override
    protected void onTextMessage(CharBuffer cb) throws IOException {
        log.debug("收到客户端发来的消息：{}", cb);
    }

    @Override
    protected void onOpen(WsOutbound outbound) {
        super.onOpen(outbound);
        synchronized (ErrorMsgServlet.sockets) {
            ErrorMsgServlet.sockets.add(this);
        }
        log.debug("客户端open");
    }

    @Override
    protected void onClose(int status) {
        super.onClose(status);
        log.debug("客户端 close");
        synchronized (ErrorMsgServlet.sockets) {
            ErrorMsgServlet.sockets.remove(this);
        }
    }
}
