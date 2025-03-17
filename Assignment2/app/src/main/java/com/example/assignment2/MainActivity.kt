package com.example.assignment2


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.assignment2.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


//import com.example.assignment2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var instruments : MutableList<Instrument>;  // Declaring the properties
    private lateinit var btn_next : Button;
    private lateinit var btn_previous : Button;
    private lateinit var btn_rent : Button;
    private lateinit var instrumentImage : ImageView;
    private lateinit var creditView : TextView;
    private lateinit var ratingBar : RatingBar;
    private lateinit var binding: ActivityMainBinding
    private var instrumentIndex : Int = 0;
    private var credit : Int = 1000;

    private lateinit var soundPool: SoundPool
    private var successSound: Int = 0;
    private var errorSound: Int = 0;
    private var buttonSound: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        instruments = mutableListOf(  // Populating the instruments
            Instrument("Guitar", R.drawable.guitar, 15, 0, 4.5f, "Perfect for beginners and seasoned musicians alike, with easy pickup and drop-off options.", mapOf(
                "Case" to 3,   // Guitar accessories list
                "Stand" to 6,
                "Extra String" to 4
            )),
            Instrument("Piano", R.drawable.piano, 65, 0, 3.5f, "Experience the elegance of sound with our beautifully crafted piano, for both beginners and experts.",mapOf(
                "Piano Bench" to 8,   // Piano accessories list
                "Sheet Music Stand" to 3,
                "Metronome" to 5
            )),
            Instrument("Violin", R.drawable.violin, 27, 0, 4.0f, "Unleash your creativity with our expertly designed violin, delivering rich tones and playability.", mapOf(
                "Case" to 3,   // Violin accessories list
                "Rosin" to 4,
                "Shoulder Rest" to 3
            )),
            Instrument("Trumpet", R.drawable.trumpet, 25, 0, 2.5f, "Our high-quality trumpet offers a bright, resonant sound ideal for performances and practice alike.", mapOf(
                "Stand" to 6,   // Trumpet accessories list
                "Mouthpiece Brush" to 4,
                "Valve Oil" to 4
            ))
        );

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //initialize the binding that control the instrument being displayed

        val window = window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        instrumentImage = findViewById(R.id.imageView) // Initializing all the Views
        btn_next = findViewById(R.id.btn_next)
        btn_previous = findViewById(R.id.btn_previous)
        btn_rent = findViewById(R.id.btn_rent)

        creditView = findViewById(R.id.credit)
        ratingBar = findViewById(R.id.ratingBar)

        btn_next.setOnClickListener(this);  // Set the on click listener event of the buttons to this class
        btn_previous.setOnClickListener(this);
        btn_rent.setOnClickListener(this);

        val audioAttributes = AudioAttributes.Builder() // Initialize sound pool
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        successSound = soundPool.load(this, R.raw.success, 1) // Initializing the sound effects
        errorSound = soundPool.load(this, R.raw.error, 1)
        buttonSound = soundPool.load(this, R.raw.button, 1)

        val stars = binding.ratingBar.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.star), PorterDuff.Mode.SRC_ATOP)

        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> // Change the Instrument rating when rating bar updated
            // Call your function here
            if(fromUser){  // Only change the instrument rating and show snack bar when rating changed by user
                instruments[instrumentIndex].rating = rating;
                showSnackBar("Rating Changed to " + rating, R.drawable.checkmark)  // Show snack bar notifying rating changed
                soundPool.play(successSound, 1f, 1f, 0, 0, 1f) // Play sound when successfully changed rating
                updateUI()
            }
        }

        updateUI()
    }

    val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ // Function run when return from Rent Activity
        if(it.resultCode == Activity.RESULT_OK){
            val updatedinstrument = it.data?.getParcelableExtra("updatedinstrument", Instrument::class.java);
                // Get the parcelable Instrument Object put by Rent Activity

            if(updatedinstrument != null){   // Only update instrument if Ren Activity actually return an Instrument object
                instruments[instrumentIndex] = updatedinstrument;
                val cost = it.data?.getIntExtra("cost", 0) // Get the total cost returned from Rent Activity
                if(cost != null){
                    credit -= cost;  // Pay the cost of renting the instrument
                }
                updateUI()

                Log.d("Instrument", "Setted Instrument")
            }
            else Log.d("Instrument", "Null Instrument")

            val message = it.data?.getStringExtra("message")
            if(message != null){
                showSnackBar(message, R.drawable.checkmark)  // Show snack bar of the returned message from Rent Activity
            }
            soundPool.play(successSound, 1f, 1f, 0, 0, 1f)
        }
    }

    fun updateUI(){

        binding.instrument = instruments[instrumentIndex]; //set the instrument being displayed
        ratingBar.rating = instruments[instrumentIndex].rating //Update the rating bar based on the instrument rating.
        instrumentImage.setImageResource(instruments[instrumentIndex].image) // Update instrument image
        creditView.setText(credit.toString())
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_next ->{                   // Switch to the next instrumnt in the list
                if(instrumentIndex >= instruments.size - 1) instrumentIndex = 0;
                else instrumentIndex++;
                updateUI()
                soundPool.play(buttonSound, 1f, 1f, 0, 0, 1f)
            }
            R.id.btn_previous ->{               // Switch to the previous instrument in the list
                if(instrumentIndex <= 0) instrumentIndex = instruments.size - 1;
                else instrumentIndex--;
                updateUI()
                soundPool.play(buttonSound, 1f, 1f, 0, 0, 1f)
            }
            R.id.btn_rent ->{  // Go the Rent Activity and pass in the current credit and the selected instrument
                val intent = Intent(this, RentActivity::class.java);
                intent.putExtra("instrument", instruments[instrumentIndex]);
                intent.putExtra("credit", credit);
                getResult.launch(intent);
                soundPool.play(buttonSound, 1f, 1f, 0, 0, 1f) // Play sound
            }
        }
    }

    @SuppressLint("RestrictedApi")
    fun showSnackBar(text : String, icon : Int){
        val snackbarView = layoutInflater.inflate(R.layout.snackbar_layout, null)  // Use custom snack bar layout
        val snackbarText : TextView = snackbarView.findViewById(R.id.snackbar_text) // Get the View in the custom snack bar layout
        val snackbarIcon : ImageView = snackbarView.findViewById(R.id.snackbar_icon)
        snackbarText.text = text; //Set the text of the snack bar
        snackbarIcon.setImageResource(icon);  // Set the snack bar icon

        val snackbar = Snackbar.make(btn_previous, "", Snackbar.LENGTH_LONG)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
        snackbarLayout.addView(snackbarView, 0)
        snackbar.show()  // Show the snack bar
    }
}