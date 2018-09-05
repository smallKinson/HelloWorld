package com.kinson.demo;

import android.app.Application;

import com.android.volley.service.VolleyService;

/**
 * Created by Kinson on 2018/8/28.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        VolleyService.init(this);
    }
}
