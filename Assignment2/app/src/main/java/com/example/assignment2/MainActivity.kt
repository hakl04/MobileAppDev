package com.example.assignment2


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.assignment2.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


//import com.example.assignment2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var instruments : MutableList<Instrument>;
    lateinit var btn_next : Button;
    lateinit var btn_previous : Button;
    lateinit var btn_rent : Button;
    lateinit var instrumentImage : ImageView;
    lateinit var creditView : TextView;
    lateinit var binding: ActivityMainBinding
    var instrumentIndex : Int = 0;
    var credit : Int = 1000;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        instruments = mutableListOf(
            Instrument("Guitar", R.drawable.guitar, 15, 0, 4.75f, "Perfect for beginners and seasoned musicians alike, with easy pickup and drop-off options.", mapOf(
                "Case" to 3,
                "Stand" to 6,
                "Extra String" to 4
            )),
            Instrument("Piano", R.drawable.piano, 65, 0, 3.75f, "Experience the elegance of sound with our beautifully crafted piano, for both beginners and experts.",mapOf(
                "Piano Bench" to 8,
                "Sheet Music Stand" to 3,
                "Metronome" to 5
            )),
            Instrument("Violin", R.drawable.violin, 27, 0, 4.25f, "Unleash your creativity with our expertly designed violin, delivering rich tones and playability.", mapOf(
                "Case" to 3,
                "Rosin" to 4,
                "Shoulder Rest" to 3
            )),
            Instrument("Trumpet", R.drawable.trumpet, 25, 0, 2.75f, "Our high-quality trumpet offers a bright, resonant sound ideal for performances and practice alike.", mapOf(
                "Stand" to 6,
                "Mouthpiece Brush" to 4,
                "Valve Oil" to 4
            ))
        );

        /*instruments = mutableListOf(
            Instrument("Guitar", R.drawable.guitar, 15, 0, 4.75f, "Perfect for beginners and seasoned musicians alike, with easy pickup and drop-off options."),
            Instrument("Piano", R.drawable.piano, 65, 0, 3.75f, "Experience the elegance of sound with our beautifully crafted piano, for both beginners and experts."),
            Instrument("Violin", R.drawable.violin, 27, 0, 4.25f, "Unleash your creativity with our expertly designed violin, delivering rich tones and playability."),
            Instrument("Trumpet", R.drawable.trumpet, 25, 0, 2.75f, "Our high-quality trumpet offers a bright, resonant sound ideal for performances and practice alike.")
        );*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main) //initialize the binging that control the instrument being displayed

        val window = window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        instrumentImage = findViewById(R.id.imageView)
        btn_next = findViewById(R.id.btn_next)
        btn_previous = findViewById(R.id.btn_previous)
        btn_rent = findViewById(R.id.btn_rent)

        creditView = findViewById(R.id.credit)

        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        btn_rent.setOnClickListener(this);

        updateUI()
    }

    val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val updatedinstrument = it.data?.getParcelableExtra("updatedinstrument", Instrument::class.java);
            if(updatedinstrument != null){
                instruments[instrumentIndex] = updatedinstrument;
                val cost = it.data?.getIntExtra("cost", 0)
                if(cost != null){
                    credit -= cost;
                }
                updateUI()
            }
            val message = it.data?.getStringExtra("message")
            if(message != null){
                showSnackBar(message, R.drawable.checkmark)
            }
        }
    }

    fun updateUI(){
        binding.instrument = instruments[instrumentIndex];
        instrumentImage.setImageResource(instruments[instrumentIndex].image)
        creditView.setText(credit.toString())
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_next ->{
                if(instrumentIndex >= instruments.size - 1) instrumentIndex = 0;
                else instrumentIndex++;
                binding.instrument = instruments[instrumentIndex];
                instrumentImage.setImageResource(instruments[instrumentIndex].image)
            }
            R.id.btn_previous ->{
                if(instrumentIndex <= 0) instrumentIndex = instruments.size - 1;
                else instrumentIndex--;
                binding.instrument = instruments[instrumentIndex];
                instrumentImage.setImageResource(instruments[instrumentIndex].image)
            }
            R.id.btn_rent ->{
                val intent = Intent(this, RentActivity::class.java);
                intent.putExtra("instrument", instruments[instrumentIndex]);
                intent.putExtra("credit", credit);
                getResult.launch(intent);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun showSnackBar(text : String, icon : Int){
        val snackbarView = layoutInflater.inflate(R.layout.snackbar_layout, null)
        val snackbarText : TextView = snackbarView.findViewById(R.id.snackbar_text)
        val snackbarIcon : ImageView = snackbarView.findViewById(R.id.snackbar_icon)
        snackbarText.text = text;
        snackbarIcon.setImageResource(icon);

        val snackbar = Snackbar.make(btn_previous, "", Snackbar.LENGTH_LONG)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
        snackbarLayout.addView(snackbarView, 0)
        snackbar.show()
    }
}