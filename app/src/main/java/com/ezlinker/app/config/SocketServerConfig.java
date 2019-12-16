package com.ezlinker.app.config;

import com.corundumstudio.socketio.MultiTypeAckCallback;
import com.corundumstudio.socketio.MultiTypeArgs;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: ezlinker
 * @description: SocketServer
 * @author: wangwenhai
 * @create: 2019-12-16 15:09
 **/
@Configuration
@Slf4j
public class SocketServerConfig {

    private static ConcurrentHashMap<String, SocketIOClient> socketIoClientStore = new ConcurrentHashMap<>();

    private static SocketIOClient socketIoClient;
    @Value("${emqx.host}")
    String emqxHost;
    @Value("${emqx.tcp-port}")
    Integer tcpPort;
    @Value("${emqx.api-port}")
    Integer apiPort;

    @Bean
    public SocketIOServer socketIoServer() {
        /*
         * 创建Socket，并设置监听端口
         */
        com.corundumstudio.socketio.Configuration socketIoConfig = new com.corundumstudio.socketio.Configuration();
        socketIoConfig.setHostname("127.0.0.1");
        socketIoConfig.setPort(2501);
        socketIoConfig.setUpgradeTimeout(10000);
        socketIoConfig.setPingTimeout(180000);
        socketIoConfig.setPingInterval(60000);
        // 认证
        socketIoConfig.setAuthorizationListener(data -> true);
        final SocketIOServer server = new SocketIOServer(socketIoConfig);
        server.addConnectListener(client -> {

            if (client != null) {
                log.info("SocketIo连接成功:" + client.getSessionId().toString());
                socketIoClient = client;
                socketIoClientStore.put(client.getSessionId().toString(), client);
            }

            socketIoClient.sendEvent("onEcho", new MultiTypeAckCallback() {

                @Override
                public void onSuccess(MultiTypeArgs objects) {

                }
            }, "来自服务器的消息:连接成功!");
        });
        server.addDisconnectListener(client -> {
            if (!socketIoClientStore.contains(client)) {
                socketIoClientStore.remove(client.getSessionId().toString());
                socketIoClient = null;
                log.info("SocketIo客户端离线成功" + client.getSessionId());

            }

        });

        /**
         * 服务端给客户端推送消息
         */
        server.addEventListener("s2c", Map.class, (client, payload, ackRequest) -> {

            System.out.println("消息到达:" + payload.toString());
            /**
             * WS的消息
             */
            echo("来自服务器的ECHO:消息发送成功:" + payload);
            /**
             * 模拟设备推送
             */
            c2s("我是来自客户端的消息:Hello I am Client001");

        });

        server.startAsync();
        return server;
    }


    /**
     * 回复消息
     * @param message
     */
    private void echo(Object message) {
        socketIoClient.sendEvent("echo", new MultiTypeAckCallback() {

            @Override
            public void onSuccess(MultiTypeArgs objects) {

            }
        }, message);
    }

    /**
     * 客户端来的消息
     * @param message
     */

    private void c2s(Object message) {
        socketIoClient.sendEvent("c2s", new MultiTypeAckCallback() {

            @Override
            public void onSuccess(MultiTypeArgs objects) {

            }
        }, message);
    }
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}
