package com.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kinson on 2018/8/28.
 */

public class SharedPreferenceUtils {
    public static final String SHAREDPREFERENCES_NAME = "system";

    /**
     * 使用 默认的文件名（SHAREDPREFERENCES_NAME = system） 保存
     * @param ctx
     * @param key
     * @param value
     */
    public static void setString(Context ctx, String key, String value) {
        setString(ctx,SHAREDPREFERENCES_NAME,key,value);
    }

    /**
     * 使用指定 文件名（fileName） 保存
     * @param ctx
     * @param fileName 文件名
     * @param key
     * @param value
     */
    public static void setString(Context ctx, String fileName, String key, String value) {
        try{
            SharedPreferences settings = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            settings.edit().putString(key, value).commit();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * 获取默认 文件名（SHAREDPREFERENCES_NAME = system） 的缓存
     * @param ctx
     * @param key
     * @return
     */
    public static String getString(Context ctx, String key) {
        return getString(ctx, SHAREDPREFERENCES_NAME, key);
    }

    /**
     * 获取指定 文件名（fileName） 的缓存
     * @param ctx
     * @param fileName
     * @param key
     * @return
     */
    public static String getString(Context ctx,String fileName, String key) {
        return getString(ctx, fileName, key,"");
    }

    /**
     * 获取指定 文件名（fileName）和 指定默认值为“” 的缓存
     * @param ctx
     * @param name
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context ctx,String name, String key, String defaultValue) {
        try{
            SharedPreferences settings = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
            return settings.getString(key, defaultValue);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 获取默认 文件名（SHAREDPREFERENCES_NAME = system）,默认值为0 的缓存
     * @param ctx
     * @param name
     * @param key
     * @return
     */
    public static int getInt(Context ctx,String name, String key) {
        return getInt(ctx, name, key,0);
    }

    /**
     * 获取指定 文件名（fileName），指定默认值 的缓存
     * @param ctx
     * @param fileName
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context ctx,String fileName, String key, int defaultValue) {
        try{
            SharedPreferences settings = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            return settings.getInt(key, defaultValue);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return  -1;
    }

    /**
     * 使用默认 文件名（SHAREDPREFERENCES_NAME = system）保存
     * @param ctx
     * @param key
     * @param value
     */
    public static void setInt(Context ctx, String key, int value) {
        setInt(ctx,SHAREDPREFERENCES_NAME,key,value);
    }

    /**
     * 使用指定 文件名（fileName）保存
     * @param ctx
     * @param fileName
     * @param key
     * @param value
     */
    public static void setInt(Context ctx, String fileName, String key, int value) {
        try{
            SharedPreferences settings = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            settings.edit().putInt(key, value).commit();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * 使用默认 文件名（SHAREDPREFERENCES_NAME = system）保存
     * @param ctx
     * @param key
     * @param value
     */
    public static void setBoolean(Context ctx, String key, boolean value) {
        setBoolean(ctx,SHAREDPREFERENCES_NAME,key,value);
    }

    /**
     * 使用指定 文件名（fileName）保存
     * @param ctx
     * @param fileName
     * @param key
     * @param value
     */
    public static void setBoolean(Context ctx, String fileName, String key, boolean value) {
        try{
            SharedPreferences settings = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            settings.edit().putBoolean(key, value).commit();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * 获取默认 文件名（SHAREDPREFERENCES_NAME = system），默认值位false 的缓存
     * @param ctx
     * @param key
     * @return
     */
    public static boolean getBoolean(Context ctx,String key) {
        return getBoolean(ctx, SHAREDPREFERENCES_NAME, key,false);
    }

    /**
     * 获取指定 文件名（fileName），默认值为false 的缓存
     * @param ctx
     * @param fileName
     * @param key
     * @return
     */
    public static boolean getBoolean(Context ctx,String fileName, String key) {
        SharedPreferences settings = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return settings.getBoolean(key, false);
    }
    /**
     * 获取指定 文件名（fileName），指定默认值 的缓存
     * @param ctx
     * @param fileName
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context ctx,String fileName, String key, boolean defaultValue) {
        SharedPreferences settings = ctx.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 清除指定 xml文件的数据
     * @param context
     * @param fileName xml文件名
     */
    public static void clearSharedPreference(Context context,String fileName){
        try{
            final SharedPreferences settings = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
            settings.edit().clear().commit();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }
}
