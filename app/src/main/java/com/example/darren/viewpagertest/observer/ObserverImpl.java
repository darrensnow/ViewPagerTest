package com.example.darren.viewpagertest.observer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.darren.viewpagertest.MainActivity;

public class ObserverImpl implements Observer {

    private static final String TAG = "ObserverImpl";

    Subject subject;
    public static String obMsg;
    private Handler handler = null;
    private MainActivity mainActivity;

    public ObserverImpl(Subject subject, Activity activity){
        this.subject = subject;
        mainActivity = (MainActivity) activity;
        subject.addObserver(this);
    }

    @Override
    public void update(String obTopicMsg, String obMsg) {
        Log.d(TAG,"topicMsg:" + obTopicMsg);
        Log.d(TAG,"obMsg:" + obMsg);
        this.obMsg = obMsg;
        handler =mainActivity.getObHandler();
        Bundle bundle = new Bundle();
        bundle.putString("obMsg",obMsg);
        Message message = new Message();
        message.what = 1;
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
