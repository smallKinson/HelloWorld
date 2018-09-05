package com.android.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by Kinson on 2018/4/24.
 */

public class GlideUtils {
    /**
     * 无占位图，加载
     * @param context
     * @param url
     * @param imageView
     */
    public static void load(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }
    /**
     * 使用占位图，加载
     * @param context
     * @param url
     * @param imageView
     * @param resid
     */
    public static void load(Context context, String url, ImageView imageView, int resid){
        Glide.with(context).load(url).placeholder(resid).into(imageView);
    }

    /**
     * 无占位图，加载的图片 为bitmap
     * @param context
     * @param url
     * @param imageView 传入自定义的圆形view
     */
    public static void loadBitmap(Context context, String url, final ImageView imageView){
        Glide.with(context).load(url).asBitmap().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                imageView.setImageBitmap(resource);
            }
        });
    }
    /**
     * 使用占位图，加载的图片 为bitmap
     * @param context
     * @param url
     * @param imageView 传入自定义的圆形view
     * @param resid
     */
    public static void loadBitmap(Context context, String url, final ImageView imageView, int resid){
        Glide.with(context).load(url).asBitmap().placeholder(resid).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                imageView.setImageBitmap(resource);
            }
        });
    }
    /**
     * 无占位图，加载的图片 转为圆形
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadCircle(final Context context, String url, final ImageView imageView){
        Glide.with(context).load(url).asBitmap().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 使用占位图，加载的图片 转为 圆形
     * @param context
     * @param url
     * @param imageView
     * @param resid
     */
    public static void loadCircle(final Context context, String url, final ImageView imageView, int resid){

        Glide.with(context).load(url).asBitmap().placeholder(resid).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    /**
     * 无占位图，加载的图片 转为 圆角
     * @param context
     * @param url
     * @param imageView
     * @param dp 圆角半径
     */
    public static void loadRound(Context context, String url, ImageView imageView, int dp) {
        Glide.with(context).load(url).transform(new CornersTransform(context, dp)).into(imageView);
    }

    /**
     * 使用占位图，加载的图片 转为 圆角
     * @param context
     * @param url
     * @param imageView
     * @param dp
     * @param resid
     */
    public static void loadRound(Context context, String url, ImageView imageView, int dp, int resid) {
        Glide.with(context).load(url).placeholder(resid).transform(new CornersTransform(context, dp)).into(imageView);
    }

    /**
     * 传入指定占位图 错误图 缓存策略 加载图片
     * @param context
     * @param url
     * @param imageView
     * @param placeHolder 占位图
     * @param errorHolder 错误图
     * @param skipMemoryCache 是否跳过内存缓存，默认为false。（false：要使用内存缓存； true：不用内存缓存）
     * @param strategy 磁盘缓存策略
     *                 1.DiskCacheStrategy.ALL:缓存原图(SOURCE)和处理图(RESULT)
     *                 2.DiskCacheStrategy.NONE:什么都不缓存
     *                 3.DiskCacheStrategy.SOURCE:只缓存原图(SOURCE)
     *                 4.DiskCacheStrategy.RESULT:只缓存处理图(RESULT) —默认值
     */
    public static void loadCustom(Context context, String url, ImageView imageView, int placeHolder, int errorHolder,
                                  boolean skipMemoryCache, DiskCacheStrategy strategy){
        Glide.with(context).load(url).skipMemoryCache(skipMemoryCache).diskCacheStrategy(strategy)
                .placeholder(placeHolder).error(errorHolder).into(imageView);
    }

    /**
     * 清除内存、磁盘缓存
     * @param context
     */
    public static void clearCache(final Context context) {
        Glide.get(context).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();

    }

}
