package com.ndhzs.bee_task1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.ArrayMap
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.ndhzs.bee_task1.http.HttpCallback
import com.ndhzs.bee_task1.http.MyHttp
import com.ndhzs.bee_task1.http.Request
import com.ndhzs.bee_task1.http.response.FailedResponse
import com.ndhzs.bee_task1.http.response.SuccessfulResponse
import com.ndhzs.bee_task1.list.MyLinkedList
import java.util.*

class MainActivity : AppCompatActivity() {

    private val http = MyHttp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            listTest()
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            httpGet()
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            httpPost()
        }
    }

    private fun listTest() {
        val list = MyLinkedList<Int>()
        repeat(10) {
            list.add(10 - it)
        }
        //10   9   8   7   6   5   4   3   2   1
        print(list)

        list.removeAt(5)//删掉索引为5的元素
        //10   9   8   7   6       4   3   2   1
        print(list)

        list.add(5, 5)//在索引为5处增加元素5
        //10   9   8   7   6   5   4   3   2   1
        print(list)

        list.remove(10)//删掉元素值为10的元素
        //     9   8   7   6   5   4   3   2   1
        print(list)

        list.sort(kotlin.Comparator { o1, o2 ->//按从小到大排序
            o1 - o2
        })
        // 1   2   3   4   5   6   7   8   9
        print(list)
    }

    private fun print(list: MyLinkedList<Int>) {
        val sb = StringBuilder()
        for (i in list) {
            sb.append("$i\t\t")
        }
        Log.d("123", sb.toString())
    }

    private fun httpGet() {
        val request = Request.Builder()
            .url("https://www.wanandroid.com/article/list/0/json")
            .get()
            .build()
        http.newCall(request).enqueue(object : HttpCallback {
            override fun onFailure(response: FailedResponse) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "GET请求失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(response: SuccessfulResponse) {
                runOnUiThread {
                    Log.d("123", "onResponse(MainActivity.kt:80)-->>  ${response.jsonObject}")
                    Toast.makeText(this@MainActivity, "GET请求成功", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun httpPost() {
        val map = ArrayMap<String, String>()
        map["username"] = "12345"
        map["password"] = "123456"
        val request = Request.Builder()
            .url("https://www.wanandroid.com/user/login")
            .post(map)
            .build()
        http.newCall(request).enqueue(object : HttpCallback {
            override fun onFailure(response: FailedResponse) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "POST请求失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(response: SuccessfulResponse) {
                runOnUiThread {
                    Log.d("123", "onResponse(MainActivity.kt:104)-->>  ${response.jsonObject}")
                    Toast.makeText(this@MainActivity, "POST请求成功", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}