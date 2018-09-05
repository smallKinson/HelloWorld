package com.android.volley.service;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kinson on 2018/8/27.
 */

public class VolleyService {

    private static final int REQ_GET = 0;
    private static final int REQ_POST = REQ_GET + 1;
    private static final int REQ_DELETE = REQ_POST + 1;
    private static final int REQ_PUT = REQ_DELETE + 1;
    private static final int MAX_CONFIG_COUNT = 5;
    private static Context mContext;
    private static VolleyService instance;
    private LinkedHashMap<String, RequestConfig> reqConfigMap;
    private HashSet<String> tagSet;
    private ReentrantLock mLock;
    private RequestQueue reqQueue;
    private RequestConfig reqConfig;

    private VolleyService() {
        reqQueue = Volley.newRequestQueue(mContext);
        tagSet = new HashSet<>();
        mLock = new ReentrantLock();
        reqConfigMap = new LinkedHashMap<>();
    }

    /**
     * 获取volleyService实例
     * @return
     */
    public static VolleyService newInstance() {
        return newInstance(null);
    }

    /**
     * 获取volleyService实例/生成实例,传入请求API链接
     * @param url
     * @return
     */
    public static VolleyService newInstance(String url) {
        if (mContext == null) {
            throw new RuntimeException("do you call init()?");
        }
        if (instance == null) {
            synchronized (VolleyService.class) {
                if (instance == null) {
                    instance = new VolleyService();
                }
            }
        }
        instance.resetReqConfig();
        instance.setUrl(url);
        return instance;
    }

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public <T> void doGet() {
        mLock.lock();
        try {
            String tag = reqConfig.getUrl();
            String cacheData = getCacheData();
            if (TextUtils.isEmpty(cacheData)) {
                saveReqConfig(tag, REQ_GET);
                ResponseListener<T> responseListener = new ResponseListener<T>(
                        this, tag, getParamsSign(), REQ_GET,
                        reqConfig.isShowError(), reqConfig.getResponse());
                if (reqConfig.getRequest() != null) {
                    reqConfig.setUrl(reqConfig.isEncoding() ? VolleyHelper
                            .concatGetUrlParams(reqConfig.getUrl(), reqConfig
                                    .getRequest().getParams(tag))
                            : VolleyHelper.concatGetUrlUnEncodingParams(
                            reqConfig.getUrl(), reqConfig.getRequest()
                                    .getParams(tag)));
                }
                GsonRequest<T> gsonRequest = new GsonRequest<T>(
                        reqConfig.getUrl(), responseListener);
                setGsonRequestParams(gsonRequest);
                execVolleyRequest(tag, gsonRequest);
            } else {
                if (reqConfig.getResponse() != null) {
                    reqConfig.getResponse().onSuccess(tag, cacheData,
                            convertCacheData(cacheData));
                }
            }
        } finally {
            mLock.unlock();
        }
    }

    /**
     * post请求
     */
    @SuppressWarnings("unchecked")
    public <T> void doPost() {
        mLock.lock();
        try {
            saveReqConfig(reqConfig.getUrl(), REQ_POST);
            ResponseListener<T> responseListener = new ResponseListener<T>(
                    this, reqConfig.getUrl(), getParamsSign(), REQ_POST,
                    reqConfig.isShowError(), reqConfig.getResponse());
            GsonRequest<T> gsonRequest = new GsonRequest<T>(Request.Method.POST,
                    reqConfig.getUrl(), responseListener);
            setGsonRequestParams(gsonRequest);
            execVolleyRequest(reqConfig.getUrl(), gsonRequest);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * delete请求
     */
    @SuppressWarnings("unchecked")
    public <T> void doDelete() {
        mLock.lock();
        try {
            String tag = reqConfig.getUrl();
            saveReqConfig(tag, REQ_DELETE);
            ResponseListener<T> responseListener = new ResponseListener<T>(
                    this, tag, getParamsSign(), REQ_DELETE,
                    reqConfig.isShowError(), reqConfig.getResponse());
            if (reqConfig.getRequest() != null) {

                reqConfig.setUrl(reqConfig.isEncoding() ? VolleyHelper
                        .concatGetUrlParams(reqConfig.getUrl(), reqConfig
                                .getRequest().getParams(tag)) : VolleyHelper
                        .concatGetUrlUnEncodingParams(reqConfig.getUrl(),
                                reqConfig.getRequest().getParams(tag)));
            }
            GsonRequest<T> gsonRequest = new GsonRequest<T>(Request.Method.DELETE,
                    reqConfig.getUrl(), responseListener);
            setGsonRequestParams(gsonRequest);
            execVolleyRequest(tag, gsonRequest);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * put请求
     */
    @SuppressWarnings("unchecked")
    public <T> void doPut() {
        mLock.lock();
        try {
            String tag = reqConfig.getUrl();
            saveReqConfig(tag, REQ_PUT);
            ResponseListener<T> responseListener = new ResponseListener<T>(
                    this, tag, getParamsSign(), REQ_PUT,
                    reqConfig.isShowError(), reqConfig.getResponse());
            if (reqConfig.getRequest() != null) {

                reqConfig.setUrl(reqConfig.isEncoding() ? VolleyHelper
                        .concatGetUrlParams(reqConfig.getUrl(), reqConfig
                                .getRequest().getParams(tag)) : VolleyHelper
                        .concatGetUrlUnEncodingParams(reqConfig.getUrl(),
                                reqConfig.getRequest().getParams(tag)));
            }
            GsonRequest<T> gsonRequest = new GsonRequest<T>(Request.Method.PUT,
                    reqConfig.getUrl(), responseListener);
            setGsonRequestParams(gsonRequest);
            execVolleyRequest(tag, gsonRequest);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * get实现请求并缓存数据
     *
     * @param dataCache
     *            -缓存
     */
    @SuppressWarnings("unchecked")
    public <T> void doGetAndRefreshCache(IDataCache dataCache) {
        mLock.lock();
        try {
            String tag = reqConfig.getUrl();
            reqConfig.setCache(dataCache);
            saveReqConfig(tag, REQ_GET);
            ResponseListener<T> responseListener = new ResponseListener<T>(
                    this, tag, getParamsSign(), REQ_GET,
                    reqConfig.isShowError(), reqConfig.getResponse());
            if (reqConfig.getRequest() != null) {
                reqConfig.setUrl(reqConfig.isEncoding() ? VolleyHelper
                        .concatGetUrlParams(reqConfig.getUrl(), reqConfig
                                .getRequest().getParams(tag)) : VolleyHelper
                        .concatGetUrlUnEncodingParams(reqConfig.getUrl(),
                                reqConfig.getRequest().getParams(tag)));
            }
            GsonRequest<T> gsonRequest = new GsonRequest<T>(reqConfig.getUrl(),
                    responseListener);
            setGsonRequestParams(gsonRequest);
            execVolleyRequest(tag, gsonRequest);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 获取缓存数据
     *
     * @return
     */
    private String getCacheData() {
        if (reqConfig.getCache() != null) {
            return reqConfig.getCache().queryCacheData(reqConfig.getUrl());
        }
        return null;
    }

    /**
     * 保存最近访问的接口配置 只有请求地址，请求类型，请求参数都相同时才认为是同一接口
     *
     * @param url
     * @param reqType
     */
    private void saveReqConfig(String url, int reqType) {
        StringBuilder key = new StringBuilder();
        key.append(url).append("&").append(reqType).append("&")
                .append(getParamsSign());
        RequestConfig cacheConfig = new RequestConfig(reqConfig);
        if (reqConfigMap.size() >= MAX_CONFIG_COUNT) {
            Iterator iterator = reqConfigMap.entrySet().iterator();
            Map.Entry entry = (Map.Entry) iterator.next();
            if (entry != null) {
                reqConfigMap.remove(entry.getKey());
            }
        }
        if (reqConfigMap.containsKey(key.toString())) {
            reqConfigMap.remove(key.toString());
        }
        reqConfigMap.put(key.toString(), cacheConfig);
    }

    /**
     * 获取参数签名串
     *
     * @return
     */
    private String getParamsSign() {
        String sign = "";
        if (reqConfig.getRequest() != null) {
            HashMap<String, String> obj = reqConfig.getRequest().getParams(
                    reqConfig.getUrl());
            HashMap<String, String> tempMap = new HashMap<String, String>();
            tempMap.putAll(obj);
            if (obj != null) {
                tempMap.remove("sid");//这里到时需要更改为 用户id的请求参数-------------------------------------------------------------------------
                sign = tempMap.toString();
            }
        }
        return sign;
    }

    /**
     * 配置请求参数
     *
     * @param gsonRequest
     */
    @SuppressWarnings("unchecked")
    private <T> void setGsonRequestParams(GsonRequest<T> gsonRequest) {
        if (reqConfig.getRequest() != null) {
            gsonRequest.setParamsMap(reqConfig.getRequest().getParams(
                    reqConfig.getUrl()));
        }
        if (reqConfig.getRequestHeaders() != null) {
            gsonRequest.setRequestHeaders(reqConfig.getRequestHeaders().getHeaders(
                    reqConfig.getUrl()));
        }
        gsonRequest.setCache(reqConfig.getCache());
        gsonRequest.setClazz(reqConfig.getTypeClazz());
        gsonRequest.setJsonKey(reqConfig.getJsonKey());
        gsonRequest.setTypeToken(reqConfig.getTypeToken());
        gsonRequest.setDataParse(reqConfig.isDataParse());
    }

    /**
     * 有网络时加入volley请求队列 ,无网络给出提示
     *
     * @param tag
     * @param gsonRequest
     */
    private <T> void execVolleyRequest(String tag, GsonRequest<T> gsonRequest) {
        if (VolleyHelper.isNetWorkAvailable(mContext)) {
            if (TextUtils.isEmpty(gsonRequest.getUrl())) {
                throw new IllegalArgumentException("request url is null?");
            }
            gsonRequest.setTag(tag);
            tagSet.add(tag);
            reqQueue.add(gsonRequest);
        } else {
//            showNetErrorHint();
            if (reqConfig.getResponse() != null) {
                reqConfig.getResponse().onFailure(tag, -1,
                        VolleyHelper.NETWORK_ERROR);
            }
        }
    }

    /**
     * 转换缓存数据
     *
     * @param cacheData
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T convertCacheData(String cacheData) {
        T result = null;
        if (reqConfig.getJsonKey() != null) {
            if (reqConfig.getTypeToken() != null) {
                result = (T) VolleyHelper.getList(cacheData, reqConfig
                        .getTypeToken().getType(), reqConfig.getJsonKey());
            }
        }
        if (reqConfig.getTypeClazz() != null) {
            if (reqConfig.getJsonKey() != null) {
                result = (T) VolleyHelper.getData(
                        VolleyHelper.parseJsonObject(cacheData,
                                reqConfig.getJsonKey()).toString(),
                        reqConfig.getTypeClazz());
            } else {
                result = (T) VolleyHelper.getData(cacheData,
                        reqConfig.getTypeClazz());
            }
        }
        return result;
    }

    /**
     * 重置请求参数
     */
    private void resetReqConfig() {
        mLock.lock();
        if (reqConfig != null) {
            reqConfig.clearAll();
        } else {
            reqConfig = new RequestConfig();
        }
        mLock.unlock();
    }

    public VolleyService setUrl(String url) {
        mLock.lock();
        reqConfig.setUrl(url);
        mLock.unlock();
        return this;
    }

    public VolleyService setJsonKey(String jsonKey) {
        mLock.lock();
        reqConfig.setJsonKey(jsonKey);
        mLock.unlock();
        return this;
    }
    //请求返回类
    public <T> VolleyService setTypeClass(Class<T> clazz) {
        mLock.lock();
        reqConfig.setTypeClazz(clazz);
        mLock.unlock();
        return this;
    }

    public <T> VolleyService setTypeToken(TypeToken<T> typeToken) {
        mLock.lock();
        reqConfig.setTypeToken(typeToken);
        mLock.unlock();
        return this;
    }

    //传参
    public VolleyService setRequest(IRequest request) {
        mLock.lock();
        reqConfig.setRequest(request);
        mLock.unlock();
        return this;
    }
    //请求回调
    public <T> VolleyService setResponse(IResponse<T> response) {
        mLock.lock();
        reqConfig.setResponse(response);
        mLock.unlock();
        return this;
    }

    public VolleyService setCache(IDataCache cache) {
        mLock.lock();
        reqConfig.setCache(cache);
        mLock.unlock();
        return this;
    }

    public VolleyService setShowError(boolean isShowError) {
        mLock.lock();
        reqConfig.setShowError(isShowError);
        mLock.unlock();
        return this;
    }

    public VolleyService setDataParse(boolean isDataParse) {
        mLock.lock();
        reqConfig.setDataParse(isDataParse);
        mLock.unlock();
        return this;
    }

    public VolleyService setEncoding(boolean isEncoding) {
        mLock.lock();
        reqConfig.setEncoding(isEncoding);
        mLock.unlock();
        return this;
    }
}
