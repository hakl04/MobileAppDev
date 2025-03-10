package com.example.week8

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var message : EditText
    lateinit var readView : TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        message = findViewById(R.id.message)
        readView = findViewById(R.id.textView)
        findViewById<Button>(R.id.button).setOnClickListener(this)
        findViewById<Button>(R.id.button2).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.button -> {
                /*var file = openFileOutput("test.txt", Context.MODE_PRIVATE)
                var message = message.text.toString()
                file.write(message.toByteArray())
                file.close()*/

                val file = File(Environment.getExternalStorageDirectory(),"/Documents/test.txt")
                file.createNewFile()
                file.writeText(message.text.toString())
            }
            R.id.button2 -> {
                /*var file = openFileInput("test.txt")
                val data = ByteArray(1024)
                file.read(data)
                readView.text = data.toString(Charsets.UTF_8)
                file.close()*/

                /*val file = File(applicationContext.filesDir,"test.txt")
                val contents = file.readText()
                readView.text = contents*/

                val file_b = File(Environment.getExternalStorageDirectory(),"/Documents/test.txt").exists()
                if(file_b){
                    val file = File(Environment.getExternalStorageDirectory(),"/Documents/test.txt");
                    readView.text = file.readText()
                }
                else {

                }
            }
        }
    }
}