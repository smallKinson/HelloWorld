package com.android.volley.service;

/**
 * Created by Kinson on 2018/8/27.
 */

public interface IDataCache {
    /**
     * 查询缓存数据
     *
     * @param tag
     *            接口tag,实际为url
     * @return
     */
    String queryCacheData(String tag);

    /**
     * 缓存接口数据
     *
     * @param tag
     *            接口tag,实际为url
     * @param cacheData
     *            缓存数据
     */
    void onSaveCacheData(String tag, String cacheData);

    /**
     * 删除所有的jsonCache缓存
     */
    void ClearAllCacheData();
}
