package com.example.week5

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skydoves.colorpickerview.ColorPickerView


class TimeActivity : AppCompatActivity(), View.OnClickListener{
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_time)
        window.decorView.setBackgroundColor(Color.BLACK)

        /*val colorPicker = findViewById<ColorPickerView>(R.id.colorPicker)
        val barBrightness = findViewById<SeekBar>(R.id.barBrightness)
        val sswitchOnOff = findViewById<Switch>(R.id.switchOnOff)*/
        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener(this);
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSave ->{
                startActivity(Intent(this, MainActivity::class.java));
            }
        }
    }
}