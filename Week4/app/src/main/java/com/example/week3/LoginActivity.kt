package com.example.week3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class LoginActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var username : EditText;
    lateinit var password : EditText;
    lateinit var button : Button;
    lateinit var viewModel : SampleViewModel;
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
        Log.d("StateCheck", "OnCreate");

        viewModel = ViewModelProvider(this).get(SampleViewModel::class.java)
        observeViewModel()

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        button = findViewById(R.id.button_login);
        button.setOnClickListener(this)

        /*val i = Intent(this, TestActivity::class.java);
        startActivity(i);*/
    }

    override fun onStart() {
        super.onStart()
        Log.d("StateCheck", "OnStart");
    }

    override fun onResume() {
        super.onResume()
        Log.d("StateCheck", "OnResume");
    }

    override fun onPause() {
        super.onPause()
        Log.d("StateCheck", "OnPause");
    }

    override fun onStop() {
        super.onStop()
        Log.d("StateCheck", "OnStop");
    }

    private fun observeViewModel(){
        viewModel.badgeCount.observe(this, Observer{
            showToast(it)
        })
    }

    private fun showToast(Value : Int){
        Toast.makeText(this, Value.toString(), Toast.LENGTH_LONG).show();
    }

    override fun onClick(v: View?) {
        viewModel.increment();
        when(v?.id){
            R.id.button_login -> {
                if(username.text.toString().equals("Hakl04") && password.text.toString().equals("pass@123")){
                    val intent = Intent(this, MainActivity::class.java);
                    intent.putExtra("username", username.text.toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}