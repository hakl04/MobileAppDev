package com.example.week5

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorListener


class MainActivity : AppCompatActivity(), View.OnClickListener{
    var brightness = 0.5f;
    var on = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.decorView.setBackgroundColor(Color.BLACK)

        val colorPicker = findViewById<ColorPickerView>(R.id.colorPicker)
        val barBrightness = findViewById<SeekBar>(R.id.barBrightness)
        val sswitchOnOff = findViewById<Switch>(R.id.switchOnOff)
        val btnSchedule = findViewById<Button>(R.id.btnSchedule)

        barBrightness.setProgress(50);

        sswitchOnOff.setOnCheckedChangeListener { _, isChecked ->
            on = isChecked
            if (on) {
                window.decorView.setBackgroundColor(Color.argb((brightness * 255).toInt(), colorPicker.color.red, colorPicker.color.green, colorPicker.color.blue))
            } else {
                window.decorView.setBackgroundColor(Color.BLACK)
            }
        }

        colorPicker.setColorListener(ColorListener { color, fromUser ->
            if (on) {
                window.decorView.setBackgroundColor(Color.argb((brightness * 255).toInt(), colorPicker.color.red, colorPicker.color.green, colorPicker.color.blue))
            }
        })

        barBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                brightness = progress / 100f
                if(on){
                    window.decorView.setBackgroundColor(Color.argb((brightness * 255).toInt(), colorPicker.color.red, colorPicker.color.green, colorPicker.color.blue))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        btnSchedule.setOnClickListener(this);
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnSchedule ->{
                startActivity(Intent(this, TimeActivity::class.java));
            }
        }
    }
}