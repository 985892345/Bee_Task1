package com.ndhzs.bee_task1.http

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/21
 *@description
 */
interface Call {
    fun request(): Request
    fun cancel()
    fun execute(httpCallback: HttpCallback)
    fun enqueue(httpCallback: HttpCallback)
    fun isCanceled(): Boolean
    fun isExecuted(): Boolean
}