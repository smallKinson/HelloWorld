package com.android.glide;

import android.content.Context;
import android.util.Log;

import com.android.utils.EnvironmentShare;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Kinson on 2018/4/24.
 */

public class CusGlideModule implements GlideModule {
    private final int DISK_CACHE_SIZE = 100 * 1024 * 1024;
    private int REQUEST_WRITE=100;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        Log.i("CusGlideModule","applyOptions");
        /**
         * 自定义内存缓存大小 自定义图片池大小
         */
        //获取内存计算器
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        //获取Glide默认内存缓存大小
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        //获取Glide默认图片池大小
        int defaultBitmapPoolSize  = calculator.getBitmapPoolSize();
        //将数值修改为之前的1.1倍
        int myMemoryCacheSize  = (int) (1.1 * defaultMemoryCacheSize);
        int myBitmapPoolSize   = (int) (1.1 * defaultBitmapPoolSize);
        //修改默认值
        builder.setMemoryCache(new LruResourceCache(myMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(myBitmapPoolSize));

        /**
         * 自定义磁盘缓存（第一次安装app不能缓存图片，因为读写sd卡权限还没动态申请）
         */
        String dirFolder = EnvironmentShare.getExternalCacheDir(context).getAbsolutePath();
        //设置磁盘缓存
        builder.setDiskCache(new DiskLruCacheFactory(dirFolder,DISK_CACHE_SIZE));

        /**
         * 自定义图片质量 (Glide默认使用RGB_565)
         *
         * ARGB_8888 :32位图,带透明度,每个像素占4个字节
         * ARGB_4444 :16位图,带透明度,每个像素占2个字节
         * RGB_565 :16位图,不带透明度,每个像素占2个字节
         * ALPHA_8 :32位图,只有透明度,不带颜色,每个像素占4个字节
         */
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);

//        builder.setDiskCacheService();//自定义本地缓存的线程池

//        builder.setResizeService();//自定义核心处理的线程池
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }


}
