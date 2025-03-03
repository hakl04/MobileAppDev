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
    var studentList : Array<String> = arrayOf("Hakl04" , "Kta87m", "freeing")
    var imageList : Array<Int> = arrayOf(R.drawable.something , R.drawable.avatar, R.drawable.discord)
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
        val data = ArrayList<DataClass>()
        for(i in 0 until studentList.size){
            data.add(DataClass(studentList[i], imageList[i]))
        }

        val adapter = RecyclerViewAdapter(data)
        lsStudent.setAdapter(adapter);

        /*lsStudent.setOnItemClickListener({ parent, view, position, id ->
            val element = arr.getItem(position) as String
            Snackbar.make(lsStudent, element, Snackbar.LENGTH_LONG).show();
        })*/
    }
}