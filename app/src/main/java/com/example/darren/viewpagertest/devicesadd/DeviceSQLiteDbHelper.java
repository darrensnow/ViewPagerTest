package com.example.darren.viewpagertest.devicesadd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DeviceSQLiteDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DeviceSQLiteDbHelper";

    private static final String CREAT_TABLE =
            "create table if not exists " + DeviceData.TABLE_NAME
            + "(deviceName text, macAddress text primary key, online text)";

    public DeviceSQLiteDbHelper(Context context){
        super(context,DeviceData.DB_NAME,null,DeviceData.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"update database......");
        String sql = "DROP TABLE IF EXISTS " + DeviceData.TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
