package com.example.week11

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView


class MainActivity : AppCompatActivity() {
    lateinit var lsValues : ListView
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

        val context = this
        val db = DBHelper(context);

        lsValues = findViewById(R.id.list)

        var strList = db.readData()
        val arr = ArrayAdapter(this, R.layout.item_layout, strList)
        lsValues.setAdapter(arr)

        findViewById<Button>(R.id.button).setOnClickListener(View.OnClickListener {
            db.insertData(findViewById<EditText>(R.id.editTextText).text.toString())
            var strList = db.readData()
            val arr = ArrayAdapter(this, R.layout.item_layout, strList)
            lsValues.setAdapter(arr)
        })
    }
}