package com.xp.app.deviceinfo

import android.content.Context

/**
 * Created by Android-小强 on 2021/4/28.
 * mailbox:980766134@qq.com
 * description: 直接从系统sdk 获取设备信息的接口类
 */
interface IQueryDevice {

    /**
     * 实时查询设备信息 存入缓存 并返回最新的信息
     */
    fun queryInfo(context: Context): DeviceData
}