package com.ndhzs.bee_task1.http

import com.ndhzs.bee_task1.http.response.FailedResponse
import com.ndhzs.bee_task1.http.response.SuccessfulResponse

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/21
 *@description
 */
interface HttpCallback {
    fun onFailure(response: FailedResponse)
    fun onResponse(response: SuccessfulResponse)
}