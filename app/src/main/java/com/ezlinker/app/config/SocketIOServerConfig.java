package com.ezlinker.app.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.ezlinker.app.config.mqtt.MqttProxyClient;
import com.ezlinker.app.config.socketio.C2SMessage;
import com.ezlinker.app.config.socketio.EchoMessage;
import com.ezlinker.app.modules.module.model.Module;
import com.ezlinker.app.modules.module.service.IModuleService;
import com.ezlinker.app.modules.mqtttopic.model.MqttTopic;
import com.ezlinker.app.modules.mqtttopic.service.IMqttTopicService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: ezlinker
 * @description: SocketServer:提供Websocket服务.注意:本类目前还没有抽象出来,代码结构搅乱,以后再提取.
 * @author: wangwenhai
 * @create: 2019-12-16 15:09
 **/
@Configuration
@Slf4j
public class SocketIOServerConfig {
    /**
     * MQTT代理
     */
    @Resource
    MqttProxyClient emqClient;
    /**
     * MQTT 配置
     */
    @Resource
    MqttConnectOptions mqttConnectOptions;

    /**
     * 模块DAO
     */
    @Resource
    IModuleService iModuleService;
    @Resource
    IMqttTopicService iMqttTopicService;
    /**
     * 记录连接进来的WS,支持多个客户端同时连接
     * K:SessionId
     * V:SocketIOClient
     */
    private static ConcurrentHashMap<String, SocketIOClient> socketIoClientStore = new ConcurrentHashMap<>();
    /**
     * K:sessionId
     * V:PublishTopic
     */
    private static ConcurrentHashMap<String, String> publishMqttTopicStore = new ConcurrentHashMap<>();

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
        server.addConnectListener(client -> {

            Long moduleId = Long.valueOf(client.getHandshakeData().getUrlParams().get("moduleId").get(0));

            socketIoClientStore.put(client.getSessionId().toString(), client);
//            EchoMessage echoMessage = new EchoMessage();
//            echoMessage.setCode(200);
//            echoMessage.setDebug(true);
//            echoMessage.setMsg("client connect successfully!");
//            echo(client, echoMessage);


            /**
             * 当WS连接成功以后,开始连接EMQX
             */
            connectToEmqx(moduleId, client);
            if (isConnectToEmqx) {
                EchoMessage m0 = new EchoMessage();
                m0.setCode(200);
                m0.setDebug(true);
                m0.setMsg("proxy connect successfully!");
                echo(client, m0);
            } else {
                EchoMessage m1 = new EchoMessage();
                m1.setCode(400);
                m1.setDebug(true);
                m1.setMsg("proxy connect failure!");
                echo(client, m1);
            }

        });
        /**
         * 离线回调
         */
        server.addDisconnectListener(client -> {
            String sessionId = client.getSessionId().toString();
            socketIoClientStore.remove(sessionId);
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

        });
        /**
         * test
         */
        /**
         *
         */
        server.addEventListener("test", Object.class, (client, payload, ackRequest) -> {
            EchoMessage testMsg = new EchoMessage();
            testMsg.setCode(201);
            testMsg.setDebug(true);
            testMsg.setMsg("test success!:" + payload.toString());
            echo(client, testMsg);

        });

        /**
         * 只有EMQ连接成功以后,WS才能推送
         */
        server.addEventListener("s2c", Object.class, (client, payload, ackRequest) -> {
            String publishTopic = publishMqttTopicStore.get(client.getSessionId().toString());
            if (isConnectToEmqx) {
                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setQos(2);
                mqttMessage.setRetained(false);
                mqttMessage.setId((int) System.nanoTime());
                mqttMessage.setPayload(payload.toString().getBytes());
                emqClient.publish(publishTopic, mqttMessage);
            }

        });

        return server;
    }


    /**
     * 回复消息
     *
     * @param message
     */
    private void echo(SocketIOClient socketIoClient, EchoMessage message) {
        socketIoClient.sendEvent("echo", JSONObject.toJSONString(message));
    }

    /**
     * 客户端来的消息
     *
     * @param message
     */

    private void c2s(SocketIOClient socketIoClient, C2SMessage message) {
        socketIoClient.sendEvent("c2s", JSONObject.toJSONString(message));
    }


    /**
     * 上报状态的消息
     *
     * @param socketIoClient
     * @param message
     */
    private void status(SocketIOClient socketIoClient, C2SMessage message) {
        socketIoClient.sendEvent("status", JSONObject.toJSONString(message));
    }

    /**
     * 代理客户端
     *
     * @param moduleId
     * @param socketIOClient
     * @return
     */
    private void connectToEmqx(Long moduleId, SocketIOClient socketIOClient) {
        /**
         * 暂时1
         */
        Module module = iModuleService.getById(moduleId);
        if (module == null) {
            isConnectToEmqx = false;
        }

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
                emqClient.connect(mqttConnectOptions);

            }
        } catch (MqttException e) {
            log.error("连接EMQX失败" + e.getMessage());
            isConnectToEmqx = false;
        }
        if (emqClient.isConnected()) {
            isConnectToEmqx = true;

            /**
             * 查询Topic
             */
            List<MqttTopic> mqttTopics = iMqttTopicService.list(new QueryWrapper<MqttTopic>().eq("module_id", module.getId()));

            for (MqttTopic topic : mqttTopics) {
                /**
                 * 用SessionID来记录Socket和Topic的关系,当转发的时候,从Map里面获取sessionId对应的 pub Topic
                 */
                if (topic.getType() == MqttTopic.S2C) {
                    publishMqttTopicStore.put(socketIOClient.getSessionId().toString(), topic.getTopic());
                }
                /**
                 * 代理首先接管客户端的数据,
                 * 因为100%确定客户端传上来的数据格式是正确的,所以这里不对[mqttMessage.getPayload()]做格式检查
                 * 直接进行转发
                 */
                if (topic.getType() == MqttTopic.C2S) {
                    try {
                        emqClient.subscribe(topic.getTopic(), 2, (s, mqttMessage) -> {
                            C2SMessage message = new C2SMessage();
                            message.setCode(200);
                            message.setMsgId(mqttMessage.getId() + "");
                            message.setDebug(true);
                            message.setData(new String(mqttMessage.getPayload(), StandardCharsets.UTF_8));
                            c2s(socketIOClient, message);
                        });
                    } catch (MqttException e) {
                        log.error("监听客户端[C2S]的topic时出现错误:" + e.getMessage());
                    }
                }
            }
        } else {
            isConnectToEmqx = false;
        }
    }
}
