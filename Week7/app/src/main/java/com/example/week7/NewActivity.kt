package com.example.week7

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class NewActivity : AppCompatActivity() {
    lateinit var lsStudent : RecyclerView
    //var studentList : Array<String> = arrayOf("Hakl04" , "Kta87m", "freeing")
    //var imageList : Array<Int> = arrayOf(R.drawable.something , R.drawable.avatar, R.drawable.discord)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.recycler_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lsStudent = findViewById(R.id.lsStudents);

        lsStudent.layoutManager = LinearLayoutManager(this)
        lsStudent.setHasFixedSize(true);
        val data = listOf(
            DataClass("Book1", 4.5f, R.drawable.book1),
            DataClass("Book2", 4.1f, R.drawable.book2),
            DataClass("Book3", 3.6f, R.drawable.book3),
            DataClass("Book4", 2.8f, R.drawable.book4),
            DataClass("Book5", 3.9f, R.drawable.book5)
        )

        val adapter = RecyclerViewAdapter(data)
        lsStudent.setAdapter(adapter);

        /*lsStudent.setOnItemClickListener({ parent, view, position, id ->
            val element = arr.getItem(position) as String
            Snackbar.make(lsStudent, element, Snackbar.LENGTH_LONG).show();
        })*/
    }
}