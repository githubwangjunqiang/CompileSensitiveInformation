package com.xp.app.deviceinfo

import android.content.Context

/**
 * Created by Android-小强 on 2021/4/28.
 * mailbox:980766134@qq.com
 * description: 缓存管理类
 */
internal class CacheManager :ICache{
    override fun saveInfo(context: Context, data: DeviceData): Boolean {
        return false
    }

    override fun loadInfo(context: Context): DeviceData? {
        return DeviceData()
    }
}