package com.example.darren.viewpagertest;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import org.apache.http.client.CookieStore;

public class MainApplication extends Application {

    public static Context context;

    private CookieStore cookieStore;

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Stetho.initializeWithDefaults(this);
    }
}
