package com.android.volley.service;

/**
 * Created by Kinson on 2018/8/27.
 */

public interface IResponse<T> {
    /*
     * 接口状态码,200表示成功
     */
    String STATUS_CODE = "statusCode";

    /*
     * 接口调用描述信息
     */
    String STATUS_MSG = "msg";
    /**
     * 接口调用成功回调
     *
     * @param tag
     *            接口tag,实际为url
     * @param srcData
     *            接口返回完整数据
     * @param t
     *            解析后的实体bean
     */
    void onSuccess(String tag, String srcData, T t);

    /**
     * 接口调用失败回调
     *
     * @param tag
     *            接口tag,实际为url
     * @param errorCode
     *            错误码
     * @param errorMsg
     *            错误描述
     */
    void onFailure(String tag, int errorCode, String errorMsg);
}
