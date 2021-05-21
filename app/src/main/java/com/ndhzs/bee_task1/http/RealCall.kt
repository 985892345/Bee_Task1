package com.ndhzs.bee_task1.http

import com.ndhzs.bee_task1.http.response.FailedResponse
import com.ndhzs.bee_task1.http.response.SuccessfulResponse
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.util.concurrent.ExecutorService

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/21
 *@description
 */
class RealCall(
    private val request: Request,
    private val threadPool: ExecutorService
) : Call {

    @Volatile
    private var isCanceled = false
    private var isExecuted = false
    private lateinit var httpCallback: HttpCallback

    override fun request(): Request {
        return request
    }

    override fun cancel() {
        isCanceled = true
    }

    override fun execute(httpCallback: HttpCallback) {
        if (isExecuted) {
            throw IllegalStateException("The call has been used")
        }
        this.httpCallback = httpCallback
        send()
    }

    override fun enqueue(httpCallback: HttpCallback) {
        if (isExecuted) {
            throw IllegalStateException("The call has been used")
        }
        this.httpCallback = httpCallback
        threadPool.execute(getSendRunnable())
    }

    override fun isCanceled(): Boolean {
        return isCanceled
    }

    override fun isExecuted(): Boolean {
        return isExecuted

    }

    private fun getSendRunnable(): Runnable {
        return Runnable {
            send()
        }
    }

    private fun send() {
        val connection = request.url.openConnection() as HttpURLConnection
        connection.requestMethod = request.method
        connection.connectTimeout = request.connectTimeout
        connection.readTimeout = request.readTimeout
        if (request.method == "POST") {
            connection.doOutput = true
            connection.doInput = true
            connection.useCaches = false
            val dataToWrite = java.lang.StringBuilder()
            val map = request.map!!
            for (key in map.keys) {
                dataToWrite.append(key).append("=").append(map[key]).append("&")
            }
            val data = dataToWrite.substring(0, dataToWrite.length - 1)
            val outputStream = connection.outputStream
            outputStream.write(data.toByteArray())
        }
        if (connection.responseCode == 200) {
            val inputStream = connection.inputStream
            val responseData = streamToString(inputStream)
            val jsonObject = JSONObject(responseData)
            if (!isCanceled) {
                httpCallback.onResponse(SuccessfulResponse(
                    request,
                    jsonObject,
                    inputStream
                ))
            }
        }else {
            if (!isCanceled) {
                httpCallback.onFailure(FailedResponse(
                    request,
                    connection.responseCode
                ))
            }
        }
        connection.disconnect()
    }

    private fun streamToString(inputStream: InputStream): String {
        val sb = StringBuilder()
        var oneLine: String?
        val reader = BufferedReader(InputStreamReader(inputStream))
        try {
            while (reader.readLine().also { oneLine = it } != null) {
                sb.append(oneLine).append('\n')
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }
}