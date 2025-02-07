package com.example.climbingapp

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var climbingGame : ClimbingGame;
    lateinit var btnClimb : Button;
    lateinit var btnFall : Button;
    lateinit var btnReset : Button;
    lateinit var holdText : TextView;
    lateinit var scoreText : TextView;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        climbingGame = ClimbingGame(0,0, false);
        getUI();
    }

    private fun getUI(){
        btnClimb = findViewById(R.id.btnClimb);
        btnFall = findViewById(R.id.btnFall);
        btnReset = findViewById(R.id.btnReset);
        btnClimb.setText(R.string.climb);
        btnFall.setText(R.string.fall);
        btnReset.setText(R.string.reset);
        holdText = findViewById(R.id.holdText);
        scoreText = findViewById(R.id.scoreText);
        btnClimb.setOnClickListener(this);
        btnFall.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        updateUI();
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnClimb ->{
                climbingGame.climb();
            }
            R.id.btnFall ->{
                climbingGame.fall();
            }
            R.id.btnReset ->{
                climbingGame.reset();
            }
        }
        updateUI()
    }

    fun updateUI(){
        holdText.setText(getString(R.string.hold) + ": " + climbingGame.getCurrentHold());
        scoreText.setText(climbingGame.getCurrentScore().toString());
        btnClimb.setEnabled(!climbingGame.getCurrentFell() && climbingGame.getCurrentHold() < 9);
        btnFall.setEnabled(!climbingGame.getCurrentFell() && climbingGame.getCurrentHold() > 0 && climbingGame.getCurrentHold() < 9);
        changeColor(climbingGame.getCurrentScore());
    }

    fun changeColor(score : Int) {
        when (score) {
            in 1..3 -> scoreText.setTextColor(Color.BLUE)
            in 4..9 -> scoreText.setTextColor(Color.GREEN)
            in 10..18 -> scoreText.setTextColor(Color.RED)
            else -> scoreText.setTextColor(Color.BLACK)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setContentView(R.layout.activity_main);
        getUI();
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("hold", climbingGame.getCurrentHold())
        outState.putInt("score", climbingGame.getCurrentScore())
        outState.putBoolean("fell", climbingGame.getCurrentFell())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        var hold : Int = savedInstanceState.getInt("hold", 0)
        var score : Int = savedInstanceState.getInt("score", 0)
        var fell : Boolean = savedInstanceState.getBoolean("fell", false)
        climbingGame = ClimbingGame(hold, score, fell);
    }
}

class ClimbingGame(hold : Int, score : Int, fell : Boolean){
    var hold : Int = 0;
    var score : Int = 0;
    var fell : Boolean = false;

    public fun getCurrentHold() : Int = hold;
    public fun getCurrentScore() : Int = score;
    public fun getCurrentFell() : Boolean = fell;

    public fun climb() : Int{
        if(!fell){
            score += when (hold) {
                in 0..2 -> 1
                in 3..5 -> 2
                in 6..8 -> 3
                else -> 0
            }
            if(hold < 9) hold ++;
            Log.d("ClimbingGame", "Climb successful");
        }
        else{
            Log.d("ClimbingGame", "Cannot climb after fell");
        }
        return getCurrentScore();
    }

    public fun fall() : Int{
        if(!fell && hold >= 1 && hold < 9){
            score -= 3;
            if(score < 0){
                score = 0;
            }
            fell = true;
            Log.d("ClimbingGame", "Fall");
        }
        else{
            Log.d("ClimbingGame", "Already fell");
        }
        return getCurrentScore();
    }

    public fun reset() : Int{
        score = 0;
        hold = 0;
        fell = false;
        Log.d("ClimbingGame", "Reset");
        return getCurrentScore();
    }
}