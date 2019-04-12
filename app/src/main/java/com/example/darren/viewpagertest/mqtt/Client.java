package com.example.darren.viewpagertest.mqtt;

import android.app.Activity;

import com.example.darren.viewpagertest.MainActivity;
import com.example.darren.viewpagertest.observer.Observer;
import com.example.darren.viewpagertest.observer.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Client{

	private Activity mainActivity;

    private static final String TAG = "MQTT_Client";

	//public static final String HOST = "tcp://10.71.255.164:1883";
	public static final String HOST = "tcp://118.24.187.74:1883";
	public static final String TOPIC = "topic01";
	private static final String clientid = "client";
	private MqttClient client;
	private MqttConnectOptions options;
	private String userName = "comm02";
	private String passWord = "123";

	private ScheduledExecutorService scheduler;

	public Client(Activity activity){
	    mainActivity = activity;
    }

	//重新链接
	public void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if (!client.isConnected()) {
					try {
						client.connect(options);
					} catch (MqttSecurityException e) {
						e.printStackTrace();
					} catch (MqttException e) {
						e.printStackTrace();
					}
				}
			}
		}, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
	}

	public void start() {
		try {
			// host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(HOST, clientid, new MemoryPersistence());
			// MQTT的连接设置
			options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(true);
			// 设置连接的用户名
			options.setUserName(userName);
			// 设置连接的密码
			options.setPassword(passWord.toCharArray());
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(10);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(20);
			// 设置回调
			client.setCallback(new PushCallback(mainActivity));
			MqttTopic topic = client.getTopic(TOPIC);
			//setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息  
			options.setWill(topic, "close".getBytes(), 0, true);
			
			client.connect(options);
			//订阅消息
			
			String[] topic1 = {TOPIC,"topic02"};
			client.subscribe(topic1);
			MqttMessage message=new MqttMessage();
			message.setQos(1);
			message.setRetained(true);
			message.setPayload("hello mosquitto,client publish message".getBytes());
			client.publish(TOPIC, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void disconnect() {
		 try {
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}


}
