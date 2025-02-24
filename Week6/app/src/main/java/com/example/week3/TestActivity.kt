package com.example.week3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TestActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var imageView : ImageView;
    var pass : String = "";
    lateinit var oldpw : EditText;
    lateinit var newpw : EditText;
    lateinit var confirmpw : EditText;

    lateinit var button_save : Button;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.testlayout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*imageView = findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.image);*/
        oldpw = findViewById(R.id.oldpw)
        newpw = findViewById(R.id.newpw)
        confirmpw = findViewById(R.id.confirmpw)
        button_save = findViewById(R.id.button_save)
        val intent = getIntent();
        val username = intent.getStringExtra("username");
        val password = intent.getStringExtra("password");
        if (password != null) {
            pass = password;
        };
        Toast.makeText(this, username + "  " + password, Toast.LENGTH_LONG).show();

        button_save.setOnClickListener(this);
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.button_save -> {
                if(oldpw.text.toString().equals(pass) && newpw.text.toString().equals(confirmpw.text.toString())){
                    val intent = Intent(this, LoginActivity::class.java);
                    //intent.putExtra("username", username.text.toString());
                    intent.putExtra("newpassword", newpw.text.toString());
                    //startActivity(intent);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    Toast.makeText(this, "Invalid Input", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}