package com.ndhzs.bee_task1.http

import java.net.URL

/**
 *@author 985892345
 *@email 2767465918@qq.com
 *@data 2021/5/21
 *@description
 */
class Request(
    val url: URL,
    val method: String,
    val connectTimeout: Int,
    val readTimeout: Int,
    val map: Map<String, String>?,
) {


    class Builder() {
        private var url: URL? = null
        private var method: String = "GET"
        private var connectTimeout: Int = 8000
        private var readTimeout: Int = 8000
        private var map: Map<String, String>? = null

        fun url(url: String): Builder {
            this.url = URL(url)
            return this
        }

        fun url(url: URL): Builder {
            this.url = url
            return this
        }

        fun get(): Builder {
            method = "GET"
            return this
        }

        fun post(map: Map<String, String>): Builder {
            this.method = "POST"
            this.map = map.toMap()
            return this
        }

        fun connectTimeout(connectTimeout: Int): Builder {
            this.connectTimeout = connectTimeout
            return this
        }

        fun readTimeout(readTimeout: Int): Builder {
            this.readTimeout = readTimeout
            return this
        }

        fun build(): Request {
            return Request(
                checkNotNull(url) { "url == null" },
                method,
                connectTimeout,
                readTimeout,
                map,
            )
        }
    }
}