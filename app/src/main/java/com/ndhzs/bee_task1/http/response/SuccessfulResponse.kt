package com.ndhzs.bee_task1.http.response

import com.ndhzs.bee_task1.http.Request
import org.json.JSONObject
import java.io.InputStream

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/21
 *@description
 */
class SuccessfulResponse(
    val request: Request,
    val jsonObject: JSONObject,
    val inputStream: InputStream
)