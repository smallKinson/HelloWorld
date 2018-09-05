package com.android.volley.service;

import android.content.Context;

import com.android.utils.SharedPreferenceUtils;

import java.util.Map;

/**
 * Created by Kinson on 2018/8/27.
 */

public class NetworkDataCache implements IDataCache {
    private static final String JSON_CACHE_FILE_NAME = "jsonCacheFileName";
    private Context context;
    private String url; // 无参数url
    private String fullUrl;// 全路径url

    public NetworkDataCache(Context context, String url, Map<String, String> params) {
        this.context = context;
        this.url = url;
        this.fullUrl = VolleyHelper.concatGetUrlParams(url, params);
        // TODO Auto-generated constructor stub
    }

    public NetworkDataCache(Context context,String url, Map<String, String> params, boolean isEncoding) {
        this.context = context;
        this.url = url;
        this.fullUrl = isEncoding ? VolleyHelper
                .concatGetUrlParams(url, params) : VolleyHelper
                .concatGetUrlUnEncodingParams(url, params);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String queryCacheData(String tag) {
        String jsonCache = SharedPreferenceUtils.getString(context, JSON_CACHE_FILE_NAME, fullUrl);
        return jsonCache;
    }

    @Override
    public void onSaveCacheData(String tag, String cacheData) {
        SharedPreferenceUtils.setString(context,JSON_CACHE_FILE_NAME,fullUrl,cacheData);
    }

    @Override
    public void ClearAllCacheData() {
        SharedPreferenceUtils.clearSharedPreference(context,JSON_CACHE_FILE_NAME);
    }
}
