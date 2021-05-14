package com.xp.app.deviceinfo

import android.content.Context

/**
 * Created by Android-小强 on 2021/4/28.
 * mailbox:980766134@qq.com
 * description: 同意管理类
 */
class DeviceManager : IDevice {
    /**
     * 缓存管理类
     */
    private val iCache: ICache by lazy {
        CacheManager()
    }

    /**
     * 查询管理类
     */
    private val iQueryDevice: IQueryDevice by lazy {
        QueryInfoManager()
    }

    private constructor()

    companion object {
        val mIDevice: IDevice by lazy {
            DeviceManager()
        }
    }

    override fun loadInfo(context: Context): DeviceData {
        val deviceData = queryInfo(context)
        iCache.saveInfo(context, deviceData)
        return deviceData
    }

    override fun loadInfoCache(context: Context): DeviceData {
        var deviceData = iCache.loadInfo(context)
        if (deviceData == null) {
            deviceData = loadInfo(context)
        }
        return deviceData
    }

    override fun queryInfo(context: Context): DeviceData = iQueryDevice.queryInfo(context)

}