package com.xp.app.deviceinfo

import android.content.Context

/**
 * Created by Android-小强 on 2021/4/28.
 * mailbox:980766134@qq.com
 * description: 查询设备信息 管理类
 */
internal class QueryInfoManager :IQueryDevice{
    override fun queryInfo(context: Context): DeviceData {
        return DeviceData()
    }
}