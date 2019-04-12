package com.example.darren.viewpagertest.devicesadd;

import android.net.Uri;
import android.provider.BaseColumns;

public class DeviceData implements BaseColumns {

    public static final String DB_NAME = "device_database.db";

    public static int DB_VERSION = 1;

    public static final String TABLE_NAME = "device";

    public static final String AUTHORITY = "com.example.darren.viewpagertest.devicesadd.DeviceDataContentProvider";

    public static final String SCHEME = "content";

    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    public static final Uri DEVICE_URI = Uri.withAppendedPath(CONTENT_URI,TABLE_NAME);

    /**
     * 信息表，及其字段
     */
    public static final String COLUMN_DEVICE_NAME = "deviceName";
    public static final String COLUMN_MACADDRESS = "macAddress";
    public static final String COLUMN_ONLINE = "online";
}
