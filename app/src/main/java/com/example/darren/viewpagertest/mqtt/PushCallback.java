package com.example.darren.viewpagertest.mqtt;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.darren.viewpagertest.MainActivity;
import com.example.darren.viewpagertest.MainApplication;
import com.example.darren.viewpagertest.devicesadd.Device;
import com.example.darren.viewpagertest.devicesadd.DeviceData;
import com.example.darren.viewpagertest.observer.Observer;
import com.example.darren.viewpagertest.observer.ObserverImpl;
import com.example.darren.viewpagertest.observer.Subject;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.util.ArrayList;
import java.util.List;

/** 
 * 发布消息的回调类 
 *  
 * 必须实现MqttCallback的接口并实现对应的相关接口方法 
 *      ◦CallBack 类将实现 MqttCallBack。每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。在回调中，将它用来标识已经启动了该回调的哪个实例。 
 *  ◦必须在回调类中实现三个方法： 
 *  
 *  public void messageArrived(MqttTopic topic, MqttMessage message) 
 *  接收已经预订的发布。 
 *  
 *  public void connectionLost(Throwable cause) 
 *  在断开连接时调用。 
 *  
 *  public void deliveryComplete(MqttDeliveryToken token)) 
 *      接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。 
 *  ◦由 MqttClient.connect 激活此回调。 
 *  
 */  
public class PushCallback implements MqttCallback,Subject{

	private static final String TAG = "PushCallback";

    private List<Observer> observerList = new ArrayList<>();
    private String obTopicMsg,obMsg;
    private Activity mainActivity;

    public PushCallback(Activity activity){
    	mainActivity = activity;
	}

	public void connectionLost(Throwable cause) {
		// 连接丢失后，一般在这里面进行重连
		Log.d(TAG,"连接断开，可以做重连");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		Log.d(TAG,"--接收消息主题--" + topic);
		Log.d(TAG,"--接收消息内容--:" + new String(message.getPayload()));
//        ObserverImpl observer = new ObserverImpl(this,mainActivity);
//		setMsg(topic,new String(message.getPayload()));
        Device device = new Device();
        device.setDevicename("device1");
        device.setMacAddress("macAddress");
        device.setOnline("online");
        MainApplication.context.getContentResolver().insert(
                DeviceData.DEVICE_URI,parseDeviceContentValues(device));
	}

    private ContentValues parseDeviceContentValues(Device device) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DeviceData.COLUMN_DEVICE_NAME,device.getDevicename());
        contentValues.put(DeviceData.COLUMN_MACADDRESS,device.getMacAddress());
        contentValues.put(DeviceData.COLUMN_ONLINE,device.getOnline());
        return contentValues;
    }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

	@Override
	public void removeObserver(Observer observer) {
		int index = observerList.indexOf(observer);
		if (index >= 0){
			observerList.remove(observer);
		}
	}

	@Override
	public void notifyObserver() {
		for (Observer observer:observerList){
			observer.update(obTopicMsg,obMsg);
		}
	}

	public void setMsg(String obTopicMsg,String obMsg){
	    this.obTopicMsg = obTopicMsg;
	    this.obMsg = obMsg;
	    Log.d(TAG,"obTopicMsg" + obTopicMsg);
	    Log.d(TAG,"obMsg" + obMsg);
	    notifyObserver();
	}

}
