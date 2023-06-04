package com.example.homework_2_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class PlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)




        val test3333 = intent.getStringExtra("key2") ?: ""
        val text7 = intent.getStringExtra("key7") ?: ""

        val chord_sec = intent.getStringExtra("key10") ?: ""
        val YTURL = intent.getStringExtra("key11") ?: ""




        val webViewYT = findViewById<WebView>(R.id.webViewYT)

        webViewYT.webViewClient = WebViewClient()

        webViewYT.apply{
            loadUrl("$YTURL")
            settings.javaScriptEnabled = true
        }






        //val chord_list = listOf("B", "Em", "Am", "F")
        //val chord_list = listOf("C", "Em", "Am", "F", "C", "Em", "Am", "F", "C", "Em", "Am", "F", "Am", "Em", "F", "C", "Em", "Am", "F", "C", "Em", "F", "F")

        //將輸入的歌曲和弦轉換成list
        val chord_list = test3333.split(" ").toList()

        //計算一個和弦有多少毫秒
        val chordSec = chord_sec.toDouble()
        val chord_change = (chordSec * 1000).toLong()
        println(chord_change)



        val imageViewChord_A = findViewById<ImageView>(R.id.imageViewChord_A)

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        buttonStart.setOnClickListener(){

            val chordListSize = chord_list.size
            var currentIndex = 0

            fun changeChordImage() {
                val chord = chord_list[currentIndex]

                when (chord) {
                    // C~B
                    "C" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_c)
                    }
                    "D" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_d)
                    }
                    "E" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_e)
                    }
                    "F" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_f)
                    }
                    "G" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_g)
                    }
                    "A" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_a)
                    }
                    "B" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_b)
                    }

                    // Cm~Bm
                    "Cm" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_cm)
                    }
                    "Dm" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_dm)
                    }
                    "Em" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_em)
                    }
                    "Fm" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_fm)
                    }
                    "Gm" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_gm)
                    }
                    "Am" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_am)
                    }
                    "Bm" -> {
                        imageViewChord_A.setImageResource(R.drawable.chord_bm)
                    }
                }

                // 顯示下一個和弦，直到顯示完最後一個和弦
                currentIndex = (currentIndex + 1) % chordListSize
                if (currentIndex != 0) {
                    Handler().postDelayed(::changeChordImage, chord_change)
                }
            }

            // 每個和弦顯示多少秒
            Handler().postDelayed(::changeChordImage, chord_change)
        }







        val buttonGo = findViewById<Button>(R.id.gohome)

        buttonGo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val text3 = test3333.toString()
            intent.putExtra("key3", text3)

            val text8 = text7
            intent.putExtra("key7", text8)

            startActivity(intent)

        }




    }
}