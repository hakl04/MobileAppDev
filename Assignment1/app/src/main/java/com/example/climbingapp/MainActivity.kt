package com.example.climbingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
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
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        climbingGame = ClimbingGame();
        btnClimb = findViewById(R.id.btnClimb);
        btnFall = findViewById(R.id.btnFall);
        btnReset = findViewById(R.id.btnReset);
        holdText = findViewById(R.id.holdText);
        scoreText = findViewById(R.id.scoreText);

        btnClimb.setOnClickListener(this);
        btnFall.setOnClickListener(this);
        btnReset.setOnClickListener(this);
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnClimb ->{
                scoreText.setText("Score: " + climbingGame.climb());
                holdText.setText("Hold: " + climbingGame.getCurrentHold());
            }
            R.id.btnFall ->{
                scoreText.setText("Score: " + climbingGame.fall());
                holdText.setText("Hold: " + climbingGame.getCurrentHold());
            }
            R.id.btnReset ->{
                scoreText.setText("Score: " + climbingGame.reset());
                holdText.setText("Hold: " + climbingGame.getCurrentHold());
            }
        }
    }
}

class ClimbingGame{
    var hold : Int = 0;
    var score : Int = 0;
    var fell : Boolean = false;

    public fun getCurrentHold() : Int = hold;
    public fun getCurrentScore() : Int = score;

    public fun climb() : Int{
        if(!fell){
            score += when (hold) {
                in 0..2 -> 1
                in 3..5 -> 2
                in 6..8 -> 3
                else -> 0
            }
            if(hold < 9) hold ++;
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
        }
        return getCurrentScore();
    }

    public fun reset() : Int{
        score = 0;
        hold = 0;
        fell = false;
        return getCurrentScore();
    }
}