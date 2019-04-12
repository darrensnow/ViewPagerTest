package com.example.darren.viewpagertest.devicesadd;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DeviceDataContentProvider extends ContentProvider {

    private static final int TABLE_DIR = 1;
    private static UriMatcher uriMatcher;
    private DeviceSQLiteDbHelper deviceSQLiteDbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DeviceData.AUTHORITY,DeviceData.TABLE_NAME,TABLE_DIR);
    }

    @Override
    public boolean onCreate() {

        deviceSQLiteDbHelper = new DeviceSQLiteDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = deviceSQLiteDbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                cursor = sqLiteDatabase.query(DeviceData.TABLE_NAME,
                        projection,selection,selectionArgs,null,null,sortOrder);

                break;
             default:
                 break;
        }
        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)){
            case TABLE_DIR:

                return "vnd.android.cursor.dri/vnd." + DeviceData.AUTHORITY + DeviceData.TABLE_NAME;

            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase sqLiteDatabase = deviceSQLiteDbHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
               long rowId = sqLiteDatabase.replace(DeviceData.TABLE_NAME,null,values);
                returnUri = Uri.parse("content://" +
                        DeviceData.AUTHORITY + "/" + DeviceData.TABLE_NAME + "/" + rowId);
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = deviceSQLiteDbHelper.getWritableDatabase();
        int deleteRow = 0;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                deleteRow = sqLiteDatabase.delete(DeviceData.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                break;
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return deleteRow;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = deviceSQLiteDbHelper.getWritableDatabase();
        int updateRow = 0;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                updateRow = sqLiteDatabase.update(DeviceData.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                break;
        }
        if (updateRow > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return updateRow;
    }
}
