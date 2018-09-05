package com.android.volley.service;

import java.util.HashMap;

/**
 * Created by Kinson on 2018/8/27.
 */

public interface IRequest {
    /**
     * 配置请求参数
     *
     * @param tag
     *            接口tag,实际为url
     * @return
     */
    HashMap<String, String> getParams(String tag);
}
