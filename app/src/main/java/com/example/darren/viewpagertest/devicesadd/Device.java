package com.example.darren.viewpagertest.devicesadd;

import android.util.Log;

public class Device {

    private static final String TAG = "Device";
    public String devicename;
    public String macAddress;
    public String online;

    public Device(){

    }

    public void setDevicename(String devicename){
        this.devicename = devicename;
    }

    public void setMacAddress(String macAddress){
        this.macAddress = macAddress;
    }

    public void setOnline(String online){
        this.online = online;
        Log.d(TAG,"online:" + online);
    }

    public String getDevicename() {
        return devicename;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getOnline() {
        return online;
    }
}
