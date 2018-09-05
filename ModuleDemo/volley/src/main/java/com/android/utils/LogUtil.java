package com.android.utils;

import android.util.Log;

/**
 * Created by Kinson on 2018/8/27.
 */

public class LogUtil {
    public static boolean isDebug=true;
    /**
     * 写日志
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg){
        if(isDebug)
            Log.e(tag, msg);
    }

    /**
     * 输出信息
     *
     * @param: String msg   信息的内容
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg){
        if(isDebug)
            Log.i(tag, msg);
    }
    /**
     * 输出信息
     *
     * @param: String msg   信息的内容
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg){
        if(isDebug)
            Log.d(tag, msg);
    }

    /**
     * 输出警告信息
     *
     * @param: String msg   信息的内容
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg){
        if(isDebug)
            Log.w(tag, msg);
    }
}
