package com.android.volley.service;

import java.util.HashMap;

/**
 * Created by Kinson on 2018/8/27.
 */

public interface IRequestHeaders {
    /**
     * 配置请求头
     *
     * @param tag
     *            接口tag,实际为url
     * @return
     */
    HashMap<String, String> getHeaders(String tag);
}
