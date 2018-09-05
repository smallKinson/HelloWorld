package com.android.volley.service;

import com.google.gson.reflect.TypeToken;

/**
 * Created by Kinson on 2018/8/27.
 */

public class RequestConfig<T> {
    private String url;
    private String jsonKey;
    private Class<T> typeClazz;
    private TypeToken<T> typeToken;
    private IRequestHeaders requestHeaders;
    private IRequest request;
    private IResponse<T> response;
    private IDataCache cache;
    private boolean isShowError = false;// 是否显示错误信息
    private boolean isDataParse = true;// 是否解析数据
    private boolean isEncoding = true;//get参数默认utf-8编码

    public RequestConfig() {

    }

    public RequestConfig(RequestConfig reqConfig) {
        if (reqConfig != null) {
            url = reqConfig.getUrl();
            jsonKey = reqConfig.getJsonKey();
            typeClazz = reqConfig.getTypeClazz();
            typeToken = reqConfig.getTypeToken();
            requestHeaders = reqConfig.getRequestHeaders();
            request = reqConfig.getRequest();
            response = reqConfig.getResponse();
            cache = reqConfig.getCache();
            isShowError = reqConfig.isShowError();
            isDataParse = reqConfig.isDataParse();
            isEncoding = reqConfig.isEncoding();
        }
    }

    public void clearAll() {
        url = null;
        jsonKey = null;
        typeClazz = null;
        typeToken = null;
        requestHeaders = null;
        request = null;
        response = null;
        cache = null;
        isShowError = false;
        isDataParse = true;
        isEncoding = true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public void setJsonKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public Class<T> getTypeClazz() {
        return typeClazz;
    }

    public void setTypeClazz(Class<T> typeClazz) {
        this.typeClazz = typeClazz;
    }

    public TypeToken<T> getTypeToken() {
        return typeToken;
    }

    public void setTypeToken(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    public IRequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(IRequestHeaders requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public IRequest getRequest() {
        return request;
    }

    public void setRequest(IRequest request) {
        this.request = request;
    }

    public IResponse<T> getResponse() {
        return response;
    }

    public void setResponse(IResponse<T> response) {
        this.response = response;
    }

    public IDataCache getCache() {
        return cache;
    }

    public void setCache(IDataCache cache) {
        this.cache = cache;
    }

    public boolean isShowError() {
        return isShowError;
    }

    public void setShowError(boolean showError) {
        isShowError = showError;
    }

    public boolean isDataParse() {
        return isDataParse;
    }

    public void setDataParse(boolean dataParse) {
        isDataParse = dataParse;
    }

    public boolean isEncoding() {
        return isEncoding;
    }

    public void setEncoding(boolean encoding) {
        isEncoding = encoding;
    }
}
