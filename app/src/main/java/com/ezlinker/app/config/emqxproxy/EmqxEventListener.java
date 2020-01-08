package com.ezlinker.app.config.emqxproxy;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.ezlinker.app.config.mqtt.MqttProxyClient;
import com.ezlinker.app.config.socketio.EchoEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @program: ezlinker
 * @description: 这里用来通过WS给前端发一些动态通知
 * @author: wangwenhai
 * @create: 2019-12-16 15:09
 **/
@Configuration
@Slf4j
public class EmqxEventListener {

    /**
     * MQTT代理
     */
    @Resource
    MqttProxyClient emqClient;

    /**
     * 状态标识:用来标识代理是否连接成功
     */
    private static boolean isConnectToEmqx = false;


    @Bean
    public SocketIOServer socketIoServer() {
        /*
         * 创建Socket，并设置监听端口
         */
        com.corundumstudio.socketio.Configuration socketIoConfig = new com.corundumstudio.socketio.Configuration();
        /**
         * 目前只允许本地WS连接
         */
        socketIoConfig.setHostname("127.0.0.1");
        /**
         * WS端口
         */
        socketIoConfig.setPort(2501);
        socketIoConfig.setUpgradeTimeout(10000);
        socketIoConfig.setPingTimeout(180000);
        socketIoConfig.setPingInterval(60000);
        // 认证
        socketIoConfig.setAuthorizationListener(data -> {
            // TODO 这里做个安全拦截器,WS必须带上颁发的随机Token才能连接
            return true;
        });
        SocketIOServer server = new SocketIOServer(socketIoConfig);
        server.startAsync();
        /**
         * WS 连接处理
         */
        /**
         * 当WS连接成功以后,开始连接EMQX
         */
        server.addConnectListener(this::connectToEmqx);
        /**
         * 离线回调
         */
        server.addDisconnectListener(this::disConnectToEmqx);

        return server;
    }


    /**
     * 回复消息
     *
     * @param message
     */
    private void echoEvent(SocketIOClient socketIoClient, EchoEventMessage message) {
        socketIoClient.sendEvent("echoEvent", JSONObject.toJSONString(message));
    }

    /**
     * 代理客户端
     *
     * @param ioClient
     * @return
     */

    private void connectToEmqx(SocketIOClient ioClient) {

        /**
         * 开始连接MQTT
         */

        try {
            /**
             * 把前一个给踢下去
             */
            if (emqClient.isConnected()) {
                emqClient.disconnect();
            } else {
                MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
                mqttConnectOptions.setConnectionTimeout(10);
                mqttConnectOptions.setCleanSession(true);
                mqttConnectOptions.setAutomaticReconnect(true);
                mqttConnectOptions.setUserName("ezlinker_event_listener");
                mqttConnectOptions.setPassword("password".toCharArray());
                emqClient.connect(mqttConnectOptions);

            }
        } catch (MqttException e) {
            e.printStackTrace();
            log.error("连接EMQX失败" + e.getMessage());
            isConnectToEmqx = false;
        }
        if (emqClient.isConnected()) {
            isConnectToEmqx = true;

            try {
                emqClient.subscribe("$SYS/brokers/+/clients/+/#", 2, (s, mqttMessage) -> {
                    EchoEventMessage m0 = new EchoEventMessage();
                    m0.setCode(200);
                    m0.setDebug(true);
                    m0.setMsg(JSONObject.parseObject(mqttMessage.toString()));
                    echoEvent(ioClient, m0);

                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            isConnectToEmqx = false;
        }
    }

    /**
     * @param ioClient
     */
    private void disConnectToEmqx(SocketIOClient ioClient) {
        if (emqClient.isConnected()) {
            try {
                emqClient.disconnect();
            } catch (MqttException e) {
                log.error("内部错误:" + e.getMessage());
            }
        }
        if (isConnectToEmqx) {
            isConnectToEmqx = false;
        }

    }
}
