package com.example.lightmeup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener{
    var on : Boolean = false;
    lateinit var image : ImageView;
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
        image = findViewById<ImageView>(R.id.image);
        image.setImageResource(R.drawable.off)
        image.setOnClickListener(this);
        image.setOnLongClickListener(this);
    }

    override fun onClick(v: View?) {
        on = !on;
        when(on){
            true -> image.setImageResource(R.drawable.on)
            false -> image.setImageResource(R.drawable.off)
            else ->{}
        }
    }

    override fun onLongClick(v: View?): Boolean {
        onClick(v);
        Toast.makeText(this, "Long Click", Toast.LENGTH_LONG).show();
        return true;
    }
}