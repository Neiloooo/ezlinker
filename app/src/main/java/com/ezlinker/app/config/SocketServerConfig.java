package com.ezlinker.app.config;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.MultiTypeAckCallback;
import com.corundumstudio.socketio.MultiTypeArgs;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.ezlinker.app.config.socketio.C2SMessage;
import com.ezlinker.app.config.socketio.EchoMessage;
import com.ezlinker.app.config.socketio.S2CMessage;
import com.ezlinker.app.modules.module.pojo.DataAreaValue;
import io.netty.channel.AbstractChannel;
import io.vertx.mqtt.MqttClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.ConnectException;
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
    @Value("${emqx.host}")
    String emqxHost;
    @Value("${emqx.tcp-port}")
    Integer tcpPort;
    @Value("${emqx.api-port}")
    Integer apiPort;

    private static ConcurrentHashMap<String, SocketIOClient> socketIoClientStore = new ConcurrentHashMap<>();

    private static SocketIOClient socketIoClient;

    /**
     * MQTT代理
     */
    @Resource
    MqttClient mqttClient;

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
            /**
             * 构造消息
             */
            EchoMessage echoMessage = new EchoMessage();
            echoMessage.setCode(201);
            echoMessage.setDebug(true);
            echoMessage.setPayload("client connected!");
            echo(echoMessage);
            /**
             * 开始连接MQTT
             */
            try {
                mqttClient.connect(tcpPort, emqxHost, mqttConnAckMessageAsyncResult -> {
                    EchoMessage e = new EchoMessage();

                    if (mqttConnAckMessageAsyncResult.succeeded()) {
                        e.setCode(201);
                        e.setDebug(true);
                        e.setPayload("proxy connected!");
                    } else {
                        e.setCode(400);
                        e.setDebug(true);
                        e.setPayload("proxy connect error!");

                    }
                    echo(e);

                });
            } catch (Exception ee) {

                EchoMessage e = new EchoMessage();
                e.setCode(400);
                e.setDebug(true);
                e.setPayload("proxy connect error!");
                echo(e);
                log.error("EMQX 连接失败");
            }


        });
        server.addDisconnectListener(client -> {
            if (!socketIoClientStore.contains(client)) {
                socketIoClientStore.remove(client.getSessionId().toString());
                socketIoClient = null;
                log.info("SocketIo客户端离线" + client.getSessionId());

            }

        });

        /**
         * 服务端给客户端推送消息
         */
        server.addEventListener("s2c", S2CMessage.class, (client, payload, ackRequest) -> {

            System.out.println("消息到达:" + payload.toString());
            /**
             * WS的消息
             */
            EchoMessage echoMessage = new EchoMessage();
            echoMessage.setCode(201);
            echoMessage.setDebug(true);
            echoMessage.setPayload("send success!");
            echo(echoMessage);

            /**
             * 模拟设备推送
             */
            C2SMessage c2SMessage = new C2SMessage();
            c2SMessage.setCode(201);
            c2SMessage.setDebug(true);
            DataAreaValue dataAreaValue = new DataAreaValue();
            dataAreaValue.setField("name");
            dataAreaValue.setValue("www");
            c2SMessage.setDataAreaValue(dataAreaValue);
            c2s(c2SMessage);
        });

        /**
         *
         */
        server.addEventListener("test", Object.class, (client, payload, ackRequest) -> {
            /**
             * WS的消息
             */
            EchoMessage echoMessage = new EchoMessage();
            echoMessage.setCode(201);
            echoMessage.setDebug(true);
            echoMessage.setPayload("test success!");
            echo(echoMessage);

        });

        server.startAsync();
        return server;
    }


    /**
     * 回复消息
     *
     * @param message
     */
    private void echo(EchoMessage message) {
        socketIoClient.sendEvent("echo", new MultiTypeAckCallback() {

            @Override
            public void onSuccess(MultiTypeArgs objects) {

            }
        }, JSONObject.toJSONString(message));
    }

    /**
     * 客户端来的消息
     *
     * @param message
     */

    private void c2s(C2SMessage message) {
        socketIoClient.sendEvent("c2s", new MultiTypeAckCallback() {

            @Override
            public void onSuccess(MultiTypeArgs objects) {

            }
        }, JSONObject.toJSONString(message));
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}
