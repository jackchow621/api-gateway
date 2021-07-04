package cn.ghost.server.websocket;

import cn.ghost.server.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 16:08
 */
@Slf4j
public class WebsocketSyncCacheServer extends WebSocketServer {

    private MessageHandler messageHandler;

    public WebsocketSyncCacheServer(String hostname, Integer port) {
        super(new InetSocketAddress(hostname, port));
        this.messageHandler = new MessageHandler();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        log.info("websocket server is open...");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        log.info("websocket server is close...");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        log.info("websocket server receive message:\n[{}]", s);
        this.messageHandler.handler(s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        log.info("websocket server is start...");
    }
}
