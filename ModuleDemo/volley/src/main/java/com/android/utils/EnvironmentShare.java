package com.android.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Kinson on 2018/4/24.
 */

public class EnvironmentShare {
    private static final String TAG = EnvironmentShare.class.getSimpleName();
    public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String ACTION_ROOT = "FHH";
    public static final String PROGRAM_ROOT = SDCARD_ROOT + File.separator + ACTION_ROOT;
    //Glide图片框架 磁盘缓存目录
    public static final String GLIDE_DISK_CACHE = PROGRAM_ROOT + File.separator+"glideDiskCache";
    //调用拍照后 的 临时图片缓存地址
    public static final String CAMERA_TEMP = PROGRAM_ROOT + File.separator+"imageUpload"+ File.separator+"photo";

    public static boolean hasSDCard(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获得Glide图片 磁盘缓存目录
     * @return File类型
     */
    public static File getGlideDiskCache(Context context){
        if(!hasSDCard()){
            return null;
        }
        String glideDiskCache = getExternalCacheDir(context).getAbsolutePath()+ File.separator+"glideDiskCache";
        File filePath = new File(glideDiskCache);
        if (!filePath.exists()) {
            // 此处可能会创建失败，暂不考虑
            boolean mkdirs = filePath.mkdirs();
            Log.i(TAG,"mkdirs="+mkdirs);
        }
        return filePath;
    }
    /**
     * 获取到 SDCard/Android/data/ 应用包名 /cache/ 目录，一般存放临时缓存数据
     * 【设置 -> 应用 -> 应用详情里面的 ” 清除缓存 “ Clear Cache 】
     * @return File类型
     */
    public static File getExternalCacheDir(Context context){
        File externalCacheDir = context.getExternalCacheDir();
        return externalCacheDir;
    }

    public static File getPhotoDir(Context context){
        if(!hasSDCard()){
            return null;
        }
        File filePath = new File(CAMERA_TEMP);
        if (!filePath.exists()) {
            // 此处可能会创建失败，暂不考虑
            boolean mkdirs = filePath.mkdirs();
            Log.i(TAG,"mkdirs="+mkdirs);
        }
        return filePath;
    }



}
