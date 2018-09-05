package com.android.volley.service;


import android.text.TextUtils;

import com.android.utils.LogUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Kinson on 2018/8/27.
 */

public class GsonRequest<T> extends Request<T> {
    private static final String TAG = GsonRequest.class.getSimpleName();
    //重连次数改为0，避免重复请求，后台重复受理
    private static final int REQUEST_MAX_RETRIES = 0; // 重连次数
    private static final int REQUEST_TIME_OUT_MILLIS = 30 * 1000;// 请求超时时间
    private boolean isDataParse;
    private ResponseListener<T> responseListener;
    private Map<String, String> paramsMap;
    private Map<String, String> headersMap;
    private TypeToken<T> typeToken;
    private Class<T> clazz;
    private String jsonKey;
    private String srcData;
    private IDataCache cache;
    public GsonRequest(String url, ResponseListener<T> responseListener) {
        this(Method.GET, url, responseListener);
        printGetUrl();
    }

    public GsonRequest(int method, String url, ResponseListener<T> responseListener) {
        super(method, url, responseListener);
        this.responseListener = responseListener;
        setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIME_OUT_MILLIS,
                REQUEST_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String resultStr = null;
        try {
            String parser = HttpHeaderParser.parseCharset(response.headers);
            if(parser.equals("ISO-8859-1")){
                parser = "UTF-8";
            }
            resultStr = new String(response.data,parser);
        } catch (Exception e) {
            try {
                srcData = new String(response.data,"UTF-8");
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        printResponseData();
        T result = null;
        int statusCode =-1;
        try {
            if(srcData.contains(IResponse.STATUS_CODE)){
                statusCode = VolleyHelper.parseStatusCode(srcData);
            }else {
                statusCode = response.statusCode;
            }
            if (statusCode == 200) {
                if (cache != null) {
                    cache.onSaveCacheData((String) getTag(), srcData);
                }
            }
            if (isDataParse) {
                if (statusCode == 200) {
                    if (jsonKey != null) {
                        if (typeToken != null) {
                            result = (T) VolleyHelper.getList(srcData,
                                    typeToken.getType(), jsonKey);
                        }
                    }
                    if (clazz != null) {
                        if (jsonKey != null) {
                            result = VolleyHelper.getData(VolleyHelper
                                    .parseJsonObject(srcData, jsonKey)
                                    .toString(), clazz);
                        } else {
                            result = VolleyHelper.getData(srcData, clazz);
                        }
                    }

                    responseListener.onInterfaceSuccess(result, srcData);
                    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                }
            } else {
                if (statusCode != VolleyHelper.LOGIN_SESSION_ERROR
                        && statusCode != VolleyHelper.ACCESS_TOKEN_EXPIRED) {
                    // 非重登录或token不失效
                    responseListener.onInterfaceSuccess(result, srcData);
                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                }
            }
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new VolleyError(VolleyHelper.UNKNOWN_ERROR));
        }
        return Response.error(new VolleyError());
    }

    @Override
    protected void deliverResponse(T response) {

    }

    @Override
    public void deliverError(VolleyError error) {
        printErrorInfo(error);
        T result = null;
        String errorData =null;
        int responseCode=-1;
        if(error.networkResponse!=null){
            responseCode = error.networkResponse.statusCode;
            errorData = new String(error.networkResponse.data);
        }else {
            errorData=error.toString();
        }
        switch (responseCode){
            default:
                if (TextUtils.isEmpty(srcData)) {
                    responseListener.onInterfaceFailure(-1, VolleyHelper.NETWORK_ERROR);
                } else {
                    responseListener.onInterfaceFailure(
                            VolleyHelper.parseStatusCode(srcData),
                            VolleyHelper.parseStatusMsg(srcData));
                }
                break;
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        printUnGetUrl();
        return paramsMap;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headersMap;
    }

    private void printGetUrl() {
        if (getMethod() == Method.GET) {
            LogUtil.d(TAG, "get_volley请求：" + getUrl());
        }
    }

    private void printUnGetUrl() {
        if (getMethod() != Method.GET) {
            LogUtil.d(TAG, getMethodDesc() + "volley请求：" + VolleyHelper.concatGetUrlUnEncodingParams(getUrl(), paramsMap));
        }
    }

    private void printResponseData() {
        LogUtil.d(TAG, getMethodDesc() + getUrl() + "_volley返回：" + srcData);
    }

    private void printErrorInfo(VolleyError error) {
        LogUtil.d(
                TAG,
                getMethodDesc()
                        + getUrl()
                        + "_volley_error返回："
                        + (error != null ? (error.networkResponse != null ? error.networkResponse.statusCode
                        + "/" + new String(error.networkResponse.data)
                        : error.toString())
                        : VolleyHelper.parseStatusMsg(srcData)));
    }

    private String getMethodDesc() {
        String req = "get_";
        if (getMethod() == Method.POST) {
            req = "post_";
        } else if (getMethod() == Method.DELETE) {
            req = "delete_";
        }
        return req;
    }

    public void setResponseListener(ResponseListener<T> responseListener) {
        this.responseListener = responseListener;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public void setRequestHeaders(Map<String, String> headersMap) {
        this.headersMap = headersMap;
    }

    public void setTypeToken(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void setJsonKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public void setCache(IDataCache cache) {
        this.cache = cache;
    }

    public void setDataParse(boolean isDataParse) {
        this.isDataParse = isDataParse;
    }
}
