package com.example.week3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var username : EditText;
    lateinit var password : EditText;
    lateinit var button : Button;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.loginlayout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        button = findViewById(R.id.button_login);
        button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.button_login -> {
                if(username.text.toString().equals("Hakl04") && password.text.toString().equals("pass@123")){
                    val intent = Intent(this, MainActivity::class.java);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}