package com.example.week7

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class HelloActivity : AppCompatActivity() {
    lateinit var lsStudent : ListView
    var studentList : Array<String> = arrayOf("Hakl04" , "Kta87m", "freeing")
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

        lsStudent = findViewById(R.id.lsStudents);
        var arr = ArrayAdapter(this, R.layout.item_layout, studentList)
        lsStudent.setAdapter(arr);

        lsStudent.setOnItemClickListener({ parent, view, position, id ->
            val element = arr.getItem(position) as String
            Snackbar.make(lsStudent, element, Snackbar.LENGTH_LONG).show();
        })
    }
}