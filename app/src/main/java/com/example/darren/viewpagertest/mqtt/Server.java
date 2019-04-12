package com.example.darren.viewpagertest.mqtt;

import android.app.Activity;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Server {

	private Activity mainActivity;
	private static final String TAG = "MQTT_Server";
	public static final String HOST = "tcp://118.24.187.74:1883";
//    public static final String HOST = "tcp://10.71.255.164:1883";

	public static final String TOPIC = "topic01";
	public static final String clientid ="server";

	public MqttClient client;
	public MqttTopic topic;
	public String userName = "comm02";
	public String passWord = "123";

	public MqttMessage message;

	public Server(Activity activity) throws MqttException {
		 //MemoryPersistence设置clientid的保存形式，默认为以内存保存
		client = new MqttClient(HOST, clientid, new MemoryPersistence());
		mainActivity = activity;
		connect();
	}
	
	private void connect() {
		MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
               client.setCallback(new PushCallback(mainActivity));
               client.connect(options);
               topic = client.getTopic(TOPIC);
        } catch (Exception e) {
               e.printStackTrace();
        }
	}
	
	public void publish(MqttMessage message) throws  MqttException{
		MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
		Log.d(TAG,token.isComplete()+"========");
	}

}