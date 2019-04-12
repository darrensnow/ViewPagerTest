package com.example.darren.viewpagertest.devicesadd;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.darren.viewpagertest.R;

import java.util.ArrayList;
import java.util.List;

public class AddDeviceActivity extends Activity {

    private static final String TAG = "AddDeviceActivity";

    private RecyclerView recyclerView;
    private AddDeviceAdapter addDeviceAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<String> list;
    private TextView add_device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initData();
        initView();
        addDevice();
    }

    private void initData() {
        list = new ArrayList<>();
    }

    private void addDevice() {
        add_device.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,"device1");
                addNewDevice();
                return true;
            }
        });
    }

    private void addNewDevice() {
        new AlertDialog.Builder(AddDeviceActivity.this)
                .setTitle("提示")
                .setMessage("添加设备")
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        list.add("device1");
                        addDeviceAdapter.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();

    }

    private void initView() {
        add_device = findViewById(R.id.add_device);
        recyclerView = findViewById(R.id.recyleview);
        addDeviceAdapter = new AddDeviceAdapter(this,list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addDeviceAdapter);
    }

}
