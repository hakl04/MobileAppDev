package com.example.week3

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var txt : TextView;
    lateinit var button : Button;
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

        txt = findViewById(R.id.txt);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button -> {
                txt.setText(R.string.test2);
                Toast.makeText(this, "Changed Text", Toast.LENGTH_LONG).show();
            }
        }
    }
}





