package com.example.homework_2_v2

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {

    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase
        //宣告 Adapter 並連結 ListView
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, items)
        findViewById<ListView>(R.id.listv).adapter = adapter
        //設定監聽器
        setListener()



        //接收歌名、和弦
        val text4 = findViewById<TextView>(R.id.textView4)
        val test44 = intent.getStringExtra("key3") ?: ""
        text4.text = test44

        val text5 = findViewById<TextView>(R.id.textView7)
        val videoname = intent.getStringExtra("key7") ?: ""
        text5.text = videoname




        //設定網址輸入框、按鈕的變數
        val buttonGo = findViewById<Button>(R.id.buttonGo)
        val editTextYTURL = findViewById<EditText>(R.id.editTextYTURL)



        //將網址變成字串放在 key1 變數傳到下一頁
        buttonGo.setOnClickListener {
            val intent = Intent(this, ChordActivity::class.java)
            val videoHTTP = editTextYTURL.text.toString()
            intent.putExtra("key1", videoHTTP)

            startActivity(intent)
        }


        val videoHTTP = editTextYTURL.text.toString()

        //新增
        val cv = ContentValues()
        cv.put("yturl",videoname)
        cv.put("chord",test44).toString()
        dbrw.insert("myTable",null,cv)

        //刪除
        dbrw.delete("myTable","yturl='http'",null)

        //查詢
        val c = dbrw.rawQuery("SELECT * FROM myTable WHERE yturl LIKE 'http'",null)
    }

    override fun onDestroy() {
        dbrw.close() //關閉資料庫
        super.onDestroy()
    }





    //設定監聽器
    private fun setListener() {
        val yturl = findViewById<EditText>(R.id.editTextYTURL)
        val chord = intent.getStringExtra("key3") ?: ""
        findViewById<Button>(R.id.newb).setOnClickListener {

            try {
                //新增一筆紀錄於 myTable 資料表
                dbrw.execSQL(
                    "INSERT INTO myTable(yturl, chord) VALUES(?,?)",
                    arrayOf(yturl.text.toString(),
                        chord)
                )
                showToast("新增:${yturl.text},和弦:${chord}")
                cleanEditText()
            } catch (e: Exception) {
                showToast("新增失敗:$e")
            }
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            //判斷是否
            if (yturl.length() < 1)
                showToast("請勿留空")
            else
                try {
                    //從 myTable 資料表刪除相同紀錄
                    dbrw.execSQL("DELETE FROM myTable WHERE yturl LIKE '${yturl.text}'")
                    showToast("刪除:${yturl.text}")
                    cleanEditText()
                } catch (e: Exception) {
                    showToast("刪除失敗:$e")
                }
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            //若無輸入則SQL語法為查詢全部書籍，反之查詢該書名資料
            val queryString = if (yturl.length() < 1)
                "SELECT * FROM myTable"
            else
                "SELECT * FROM myTable WHERE yturl LIKE '${yturl.text}'"
            val c = dbrw.rawQuery(queryString, null)
            c.moveToFirst() //從第一筆開始輸出
            items.clear() //清空舊資料
            showToast("共有${c.count}筆資料")
            for (i in 0 until c.count) {
                //加入新資料
                items.add("名稱:${c.getString(0)}\t\t\t\t 和弦:${c.getString(1)}")
                c.moveToNext() //移動到下一筆
            }
            adapter.notifyDataSetChanged() //更新列表資料
            c.close() //關閉 Cursor
        }
        findViewById<Button>(R.id.deleall).setOnClickListener {
            try {
                //刪除 myTable 資料表中的所有資料
                dbrw.execSQL("DELETE FROM myTable")
                showToast("已刪除所有資料")
                cleanEditText()
            } catch (e: Exception) {
                showToast("刪除失敗:$e")
            }
        }
        findViewById<Button>(R.id.dele1).setOnClickListener {
            try {
                //取得最後資訊
                val queryString = "SELECT MAX(rowid) FROM myTable"
                val cursor = dbrw.rawQuery(queryString, null)
                cursor.moveToFirst()
                val lastRowId = cursor.getLong(0)
                cursor.close()

                //删除
                dbrw.execSQL("DELETE FROM myTable WHERE rowid = $lastRowId")
                showToast("已刪除上一筆資料")
                cleanEditText()
            } catch (e: Exception) {
                showToast("刪除失敗:$e")
            }
        }

    }
    //建立 showToast 方法顯示 Toast 訊息
    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()
    //清空輸入
    private fun cleanEditText() {
        findViewById<EditText>(R.id.editTextYTURL).setText("")

    }


}


