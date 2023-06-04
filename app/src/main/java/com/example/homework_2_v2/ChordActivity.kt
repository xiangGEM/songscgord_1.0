package com.example.homework_2_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


class ChordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chord)

        // 接收上一頁的網址
        val YTURL = intent.getStringExtra("key1") ?: ""
        val videoID: String? = when {
            YTURL.startsWith("https://www.youtube.com/watch?v=") -> YTURL.substringAfter("https://www.youtube.com/watch?v=")
            YTURL.startsWith("https://youtu.be/") -> YTURL.substringAfter("https://youtu.be/")
            else -> null
        }
        

        // 連接網路服務
        val client = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()


        val url = "http://192.168.0.110:5000/calculate_chord_sec"
        // 替換為計算機的IP地址

        // 設定POST請求的Body
        val requestBody = FormBody.Builder()
            .add("youtube_url", YTURL)
            .build()

        // 設定POST請求
        val requestYTAPI = Request.Builder()
            .post(requestBody)
            .url(url)
            .build()

        // 使用 CountDownLatch 来等待 chord_sec 返回
        val latch = CountDownLatch(1)





        // 建立OkHttpClient並發送請求
        client.newCall(requestYTAPI).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                val json = Gson().fromJson(responseBody, JsonObject::class.java)
                val chordSec = json.get("chord_sec").asDouble
                val duration = json.get("duration").asDouble
                val tempo = json.get("tempo").asInt
                val youenter = duration/chordSec

                // 將chordSec顯示在TextView上
                runOnUiThread {
                    val textView2 = findViewById<TextView>(R.id.textView2)
                    textView2.text = "$chordSec"
                    //val textView5 = findViewById<TextView>(R.id.textView5)
                    //textView5.text = "影片時間: ${duration.toInt()}"
                    //val textView6 = findViewById<TextView>(R.id.textView6)
                    //textView6.text = "需要輸入(個): ${youenter.toInt()}"
                    val textView8 = findViewById<TextView>(R.id.textView8)
                    textView8.text = "$tempo"
                }
            }


            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })

        // 連接YouTube API獲取影片標題
        val urlYTAPI = "https://www.googleapis.com/youtube/v3/videos?id=$videoID&part=snippet&key=AIzaSyDZF2AHLOUOUkwEX0xaaoG0UjUWNN4oJ3Q"

        val request = Request.Builder()
            .url(urlYTAPI)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    println(responseBody)

                    val gson = Gson()
                    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                    val YTAPI_jsonObject = gson.fromJson(responseBody, JsonObject::class.java)


                    // 取得 YouTube 影片的標題、頻道名稱
                    val title = jsonObject
                        .getAsJsonArray("items")[0]
                        .asJsonObject
                        .getAsJsonObject("snippet")
                        .get("title")
                        .asString
                    println("標題---------${title}")

                    val singer = YTAPI_jsonObject
                        .getAsJsonArray("items")[0]
                        .asJsonObject
                        .getAsJsonObject("snippet")
                        .get("channelTitle")
                        .asString
                    println("频道名字---------${singer}")

                    runOnUiThread {
                        findViewById<TextView>(R.id.textViewShowTitle).text = title.toString()
                        findViewById<TextView>(R.id.textViewShowAll).text = singer
                    }
                } else {
                    println("Request failed")
                    runOnUiThread {
                        findViewById<TextView>(R.id.textViewShowAll).text = "資料錯誤"
                    }
                }
            }
        })
        //設定網址輸入框、按鈕的變數
        val buttonGo = findViewById<Button>(R.id.nextpage)
        val editTextYTURL = findViewById<EditText>(R.id.entchord)



        //將網址變成字串放在 key2,7 變數傳到下一頁
        buttonGo.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            val text2 = editTextYTURL.text.toString()
            intent.putExtra("key2", text2)

            val text7 = findViewById<TextView>(R.id.textViewShowTitle).text
            intent.putExtra("key7", text7)

            val textView8 = findViewById<TextView>(R.id.textView2).text
            intent.putExtra("key10", textView8)


            intent.putExtra("key11", YTURL)

            startActivity(intent)

        }
    }
}
