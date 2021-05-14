package com.xp.app.deviceinfo

import android.content.Context

/**
 * Created by Android-小强 on 2021/4/28.
 * mailbox:980766134@qq.com
 * description: 设备信息采集 接口
 */
interface IDevice {

    /**
     * 获取设备信息
     */
    fun loadInfo(context: Context): DeviceData

    /**
     * 获取设备信息 缓存有 就拿缓存的信息 缓存没有 就会调用 loadInfo()获取
     */
    fun loadInfoCache(context: Context): DeviceData

    /**
     * 实时查询设备信息  并返回最新的信息
     */
    fun queryInfo(context: Context): DeviceData



}