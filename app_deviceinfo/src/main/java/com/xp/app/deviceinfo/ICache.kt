package com.xp.app.deviceinfo

import android.content.Context

/**
 * Created by Android-小强 on 2021/4/28.
 * mailbox:980766134@qq.com
 * description: 缓存 接口
 */
interface ICache {
    /**
     * 存入缓存
     */
    fun saveInfo(context: Context,data: DeviceData): Boolean

    /**
     * 從緩存獲取
     */
    fun loadInfo(context: Context):DeviceData?
}