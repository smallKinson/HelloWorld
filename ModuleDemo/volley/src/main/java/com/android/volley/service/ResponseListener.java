package com.android.volley.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Kinson on 2018/8/27.
 */

public class ResponseListener<T> implements Response.ErrorListener {
    private VolleyService volleyService;
    private IResponse<T> response;
    private boolean isShowError;
    private String urlTag;// 请求接口地址
    private String paramsSign; // 用于重登录时参数标识
    private int reqType;// 请求类型
    private static String preReqUrl;// 上一次重连url，保证同一时间段只重连一次

    public ResponseListener(VolleyService volleyService, String tag,
                            String paramsSign, int reqType, boolean isShowError,
                            IResponse<T> response) {
        this.volleyService = volleyService;
        this.urlTag = tag;
        this.paramsSign = paramsSign;
        this.reqType = reqType;
        this.response = response;
        this.isShowError = isShowError;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (response != null && error != null) {
            int status = -1;
            if (error.networkResponse != null) {
                status = error.networkResponse.statusCode;
            }
            if (isShowError) {
//                ToastAlert.showMsg("server error：" + status);
            }
            final int newStatus = status;
            response.onFailure(urlTag, newStatus, error.getMessage());
        }

    }

    /**
     * 接口成功回调
     *
     * @param arg0
     * @param srcData
     */
    public void onInterfaceSuccess(final T arg0, final String srcData) {
        if (response != null) {
            response.onSuccess(urlTag, srcData, arg0);
        }
    }

    /**
     * 接口失败回调
     *
     * @param errorCode
     * @param errorMsg
     */
    public void onInterfaceFailure(final int errorCode, final String errorMsg) {
        if (response != null) {
            response.onFailure(urlTag, errorCode, errorMsg);
        }
    }
}
