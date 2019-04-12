package com.example.darren.viewpagertest;

import android.Manifest;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.security.keystore.KeyGenParameterSpec;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.darren.viewpagertest.RoundImage.GlideCircleTransform;
import com.example.darren.viewpagertest.Upgrade.UpdateAppManager;
import com.example.darren.viewpagertest.Upgrade.UpdateUtils;
import com.example.darren.viewpagertest.banner.Banner;
import com.example.darren.viewpagertest.banner.GlideImageLoader;
import com.example.darren.viewpagertest.banner.HttpUtils;
import com.example.darren.viewpagertest.banner.JsonUtils;
import com.example.darren.viewpagertest.devicesadd.AddDeviceActivity;
import com.example.darren.viewpagertest.devicesadd.Device;
import com.example.darren.viewpagertest.devicesadd.DeviceData;
import com.example.darren.viewpagertest.mqtt.Client;
import com.example.darren.viewpagertest.mqtt.Server;
import com.example.darren.viewpagertest.test.ConnectActivity;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener , android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "MainActivity";

    private final int LOADER_ID = 1;

    private String online_Text = null;

    public Handler obHandler;

    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;

    private Banner banner;
    private Button check_update,smart_connect,add_device,mqtt_client,mqtt_server;
    private TextView online;
    private final String URL_PATH_JSON = "http://118.24.187.74:8080/voicebox/ads/getAds";
    private final String UPDATE_URL_PATH_JSON = "http://118.24.187.74:8080/voiceboxAdmin/api/apk/version?version=0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        initHandler();
        initView();
        setOnClickListener();
        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }

    @Override
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(LOADER_ID);
        super.onDestroy();
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        if (id == LOADER_ID){
            return new android.support.v4.content.CursorLoader(this,DeviceData.DEVICE_URI,null,null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == LOADER_ID){
            if (data != null && data.moveToFirst()){
                List<Device> list = new ArrayList<>();
//                while (data.moveToNext()){
//                    list.add(parseDevice(data));
//                    Log.d(TAG,"list.add");
//                }
                do {
                    list.add(parseDevice(data));
                }while (data.moveToNext());
                if (list.size() != 0){
                    this.online_Text = list.get(0).online;
                    online = findViewById(R.id.online);
                    online.setText(this.online_Text);
                }else {
                    online = findViewById(R.id.online);
                    online.setText("not online");
                }
            }
        }
    }

    private Device parseDevice(Cursor cursor) {
        Device device = new Device();
        device.setDevicename(cursor.getString(cursor.getColumnIndex("deviceName")));
        device.setMacAddress(cursor.getString(cursor.getColumnIndex("macAddress")));
        device.setOnline(cursor.getString(cursor.getColumnIndex("online")));
        return device;
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }

    public Handler getObHandler(){
        return obHandler;
    }

    public void setHandler(Handler handler){
        obHandler = handler;
    }
    private void initHandler() {
        obHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        Bundle bundle = msg.getData();
                        String obMessage = bundle.getString("obMsg");
                        Log.d(TAG,"obMessage:" + obMessage);
                        online = findViewById(R.id.online);
                        online.setText(obMessage);
                        break;
                }
            }
        };
    }

    private void initView() {
        banner = findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        new MyAsyncTask1().execute(URL_PATH_JSON);

        check_update = findViewById(R.id.check_update);
        smart_connect = findViewById(R.id.bt_smart_connect);
        add_device = findViewById(R.id.jp_add_device);

        mqtt_client = findViewById(R.id.mqtt_client);
        mqtt_server = findViewById(R.id.mqtt_server);

        ImageView imageView = findViewById(R.id.avatar);
        Glide.with(this)
                .load(R.drawable.load)
                .error(R.drawable.error)
                .transform(new GlideCircleTransform(this))
                .into(imageView);


    }

    private void setOnClickListener(){
        check_update.setOnClickListener(this);
        smart_connect.setOnClickListener(this);
        add_device.setOnClickListener(this);
        mqtt_client.setOnClickListener(this);
        mqtt_server.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_update:
                new CheckUpdateTask().execute(UPDATE_URL_PATH_JSON);
                break;
            case R.id.bt_smart_connect:
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intent);
                break;
            case R.id.jp_add_device:
                Intent intent1 = new Intent(MainActivity.this, AddDeviceActivity.class);
                Log.d(TAG,"add device");
                startActivity(intent1);
                break;
            case R.id.mqtt_client:
                    Client client = new Client(MainActivity.this);
                    client.start();
                    Log.d(TAG,"start mqtt client");
                break;
            case R.id.mqtt_server:
                try {
                    Server server =  new Server(MainActivity.this);
                    server.message = new MqttMessage();
                    server.message.setQos(1);
                    server.message.setRetained(true);
                    server.message.setPayload("eeeeeaaaaaawwwwww---".getBytes());
                    server.publish(server.message);
                    Log.d(TAG,server.message.isRetained()+"------ratained状态");
                }catch (MqttException e){
                    Log.d(TAG,"server error");
                    e.printStackTrace();
                }catch (Exception e){
                    Log.d(TAG,"server error");
                }
                break;
        }
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }


    public class CheckUpdateTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG,"check update");
            String downloadUrl;
            String urlString = HttpUtils.sendGetMessage(strings[0], "utf-8");
            Log.d(TAG,"urlString:" + urlString);
            downloadUrl = UpdateUtils.parseUrl(urlString);
            Log.d(TAG,"downloadUrl:" + downloadUrl);
            return downloadUrl;
        }

        @Override
        protected void onPostExecute(String downloadUrl) {
            Log.d(TAG,"downloadUrl" + downloadUrl);
            Toast.makeText(MainActivity.this,"update available",Toast.LENGTH_SHORT).show();
            download(downloadUrl);
        }

        private void download(final String downloadUrl) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("版本更新")
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            UpdateAppManager.downloadApk(MainActivity.this,downloadUrl,"版本升级","SmartBox");

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
    }

    public class MyAsyncTask1 extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> urls = new ArrayList<>();
            String urlString = HttpUtils.sendPostMessage(strings[0], "utf-8");
            //    解析服务器端的json数据
            urls = JsonUtils.parseUrl(urlString);
            return urls;
        }

        @Override
        protected void onPostExecute(List<String> urls) {
            banner.setImages(urls);
            banner.start();
        }
    }
}
