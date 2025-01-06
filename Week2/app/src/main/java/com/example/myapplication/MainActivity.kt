package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var plus : Button;
    lateinit var minus : Button;
    lateinit var multiply : Button;
    lateinit var divide : Button;
    lateinit var num1 : EditText;
    lateinit var num2 : EditText;
    lateinit var result : TextView;
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

        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        multiply = findViewById(R.id.multiply);
        divide = findViewById(R.id.divide);
        result = findViewById(R.id.result);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        multiply.setOnClickListener(this);
        divide.setOnClickListener(this);

        var Cars = arrayOf("Volvo", "Toyota", "Kia")
        Cars.set(1,"Hyundai");
        Toast.makeText(this, Cars.get(1), Toast.LENGTH_LONG).show();

        for(i in 6 downTo 0 step 2){

        }

        var s : String = "";
        checkpoint@ for(i in Cars.indices){
            if(Cars.get(i) == "Hyindai") break@checkpoint
            s += Cars.get(i);
        }

        var r : String = "";
        checkpoint@ for(i in Cars.indices){
            if(Cars.get(i) == "Hyindai") continue@checkpoint
            r += Cars.get(i);
        }
    }

    fun test1(a : Int, b : Int) = a + b;

    fun test2(a : Int, b : Int) : Int {
        return a + b;
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.plus ->{
                result.setText("Result: " + (num1.getText().toString().toDouble() + num2.getText().toString().toDouble()));
            }
            R.id.minus ->{
                result.setText("Result: " + (num1.getText().toString().toDouble() - num2.getText().toString().toDouble()));
            }
            R.id.multiply ->{
                result.setText("Result: " + (num1.getText().toString().toDouble() * num2.getText().toString().toDouble()));
            }
            R.id.divide ->{
                result.setText("Result: " + (num1.getText().toString().toDouble() / num2.getText().toString().toDouble()));
            }
        }
    }

}
