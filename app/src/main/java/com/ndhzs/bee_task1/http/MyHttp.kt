package com.ndhzs.bee_task1.http

import java.util.concurrent.Executors

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/19
 *@description
 */
class MyHttp() {

    private val cachedThreadPoll = Executors.newCachedThreadPool()

    fun newCall(request: Request): Call {
        return RealCall(request, cachedThreadPoll)
    }
}