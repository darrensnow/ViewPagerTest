/**
 * aliyun.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.example.darren.viewpagertest.mqtt;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * IoT濂椾欢JAVA鐗堣澶囨帴鍏emo
 */
public class SimpleClient4IOT {
	
	/******杩欓噷鏄鎴风闇�瑕佺殑鍙傛暟*******/
    public static String deviceName = "";
    public static String productKey = "";
    public static String secret = "";

    //鐢ㄤ簬娴嬭瘯鐨則opic
    private static String subTopic = "/" + productKey + "/" + deviceName + "/get";
    private static String pubTopic = "/" + productKey + "/" + deviceName + "/update";

    public static void main(String... strings) throws Exception {
        //瀹㈡埛绔澶囪嚜宸辩殑涓�涓爣璁帮紝寤鸿鏄疢AC鎴朣N锛屼笉鑳戒负绌猴紝32瀛楃鍐�
        String clientId = InetAddress.getLocalHost().getHostAddress();

        //璁惧璁よ瘉
        Map<String, String> params = new HashMap<String, String>();
        params.put("productKey", productKey); //杩欎釜鏄搴旂敤鎴峰湪鎺у埗鍙版敞鍐岀殑 璁惧productkey
        params.put("deviceName", deviceName); //杩欎釜鏄搴旂敤鎴峰湪鎺у埗鍙版敞鍐岀殑 璁惧name
        params.put("clientId", clientId);
        String t = System.currentTimeMillis() + "";
        params.put("timestamp", t);

        //MQTT鏈嶅姟鍣ㄥ湴鍧�锛孴LS杩炴帴浣跨敤ssl寮�澶�
        String targetServer = "ssl://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";

        //瀹㈡埛绔疘D鏍煎紡锛屼袱涓獆|涔嬮棿鐨勫唴瀹逛负璁惧绔嚜瀹氫箟鐨勬爣璁帮紝瀛楃鑼冨洿[0-9][a-z][A-Z]
        String mqttclientId = clientId + "|securemode=2,signmethod=hmacsha1,timestamp=" + t + "|";
        String mqttUsername = deviceName + "&" + productKey; //mqtt鐢ㄦ埛鍚嶆牸寮�
//        String mqttPassword = SignUtil.sign(params, secret, "hmacsha1"); //绛惧悕

        System.err.println("mqttclientId=" + mqttclientId);

//        connectMqtt(targetServer, mqttclientId, mqttUsername, mqttPassword, deviceName);
    }

    public static void connectMqtt(String url, String clientId, String mqttUsername,
                                   String mqttPassword, final String deviceName) throws Exception {
        MemoryPersistence persistence = new MemoryPersistence();
        SSLSocketFactory socketFactory = createSSLSocket();
        final MqttClient sampleClient = new MqttClient(url, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setMqttVersion(4); // MQTT 3.1.1
        connOpts.setSocketFactory(socketFactory);

        //璁剧疆鏄惁鑷姩閲嶈繛
        connOpts.setAutomaticReconnect(true);

        //濡傛灉鏄痶rue锛岄偅涔堟竻鐞嗘墍鏈夌绾挎秷鎭紝鍗砆oS1鎴栬��2鐨勬墍鏈夋湭鎺ユ敹鍐呭
        connOpts.setCleanSession(false);

        connOpts.setUserName(mqttUsername);
        connOpts.setPassword(mqttPassword.toCharArray());
        connOpts.setKeepAliveInterval(65);

//        LogUtil.print(clientId + "杩涜杩炴帴, 鐩殑鍦�: " + url);
        sampleClient.connect(connOpts);

        sampleClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
//                LogUtil.print("杩炴帴澶辫触,鍘熷洜:" + cause);
                cause.printStackTrace();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                LogUtil.print("鎺ユ敹鍒版秷鎭�,鏉ヨ嚦Topic [" + topic + "] , 鍐呭鏄�:["
//                    + new String(message.getPayload(), "UTF-8") + "],  ");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //濡傛灉鏄疩oS0鐨勬秷鎭紝token.resp鏄病鏈夊洖澶嶇殑
//                LogUtil.print("娑堟伅鍙戦�佹垚鍔�! " + ((token == null || token.getResponse() == null) ? "null"
//                    : token.getResponse().getKey()));
            }
        });
//        LogUtil.print("杩炴帴鎴愬姛:---");

        //杩欓噷娴嬭瘯鍙戦�佷竴鏉℃秷鎭�
        String content = "{'content':'msg from :" + clientId + "," + System.currentTimeMillis() + "'}";

        MqttMessage message = new MqttMessage(content.getBytes("utf-8"));
        message.setQos(0);
        //System.out.println(System.currentTimeMillis() + "娑堟伅鍙戝竷:---");
        sampleClient.publish(pubTopic, message);

        //涓�娆¤闃呮案涔呯敓鏁� 
        //杩欎釜鏄涓�绉嶈闃卼opic鏂瑰紡锛屽洖璋冨埌缁熶竴鐨刢allback
        sampleClient.subscribe(subTopic);

        //杩欎釜鏄浜岀璁㈤槄鏂瑰紡, 璁㈤槄鏌愪釜topic锛屾湁鐙珛鐨刢allback
        //sampleClient.subscribe(subTopic, new IMqttMessageListener() {
        //    @Override
        //    public void messageArrived(String topic, MqttMessage message) throws Exception {
        //
        //        LogUtil.print("鏀跺埌娑堟伅锛�" + message + ",topic=" + topic);
        //    }
        //});

        //鍥炲RRPC鍝嶅簲
        final ExecutorService executorService = new ThreadPoolExecutor(2,
            4, 600, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(100), new CallerRunsPolicy());

        String reqTopic = "/sys/" + productKey + "/" + deviceName + "/rrpc/request/+";
        sampleClient.subscribe(reqTopic, new IMqttMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                LogUtil.print("鏀跺埌璇锋眰锛�" + message + ", topic=" + topic);
                String messageId = topic.substring(topic.lastIndexOf('/') + 1);
                final String respTopic = "/sys/" + productKey + "/" + deviceName + "/rrpc/response/" + messageId;
                String content = "hello world";
                final MqttMessage response = new MqttMessage(content.getBytes());
                response.setQos(0); //RRPC鍙敮鎸丵oS0
                //涓嶈兘鍦ㄥ洖璋冪嚎绋嬩腑璋冪敤publish锛屼細闃诲绾跨▼锛屾墍浠ヤ娇鐢ㄧ嚎绋嬫睜
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sampleClient.publish(respTopic, response);
//                            LogUtil.print("鍥炲鍝嶅簲鎴愬姛锛宼opic=" + respTopic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private static SSLSocketFactory createSSLSocket() throws Exception {
        SSLContext context = SSLContext.getInstance("TLSV1.2");
        context.init(null, new TrustManager[]{}, null);
//        context.init(null, new TrustManager[] {new ALiyunIotX509TrustManager()}, null);
        SSLSocketFactory socketFactory = context.getSocketFactory();
        return socketFactory;
    }
}
