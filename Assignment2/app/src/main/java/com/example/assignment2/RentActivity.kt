package com.example.assignment2


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.assignment2.databinding.ActivityMainBinding
import com.example.assignment2.databinding.ActivityRentBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar

//import com.example.assignment2.databinding.ActivityMainBinding


class RentActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var instrument : Instrument;   // Declaring the properties
    private lateinit var creditView : TextView;
    private lateinit var total_cost : TextView;
    private lateinit var btn_cancel : Button;
    private lateinit var btn_save : Button;
    private lateinit var binding : ActivityRentBinding
    private lateinit var instrumentImage : ImageView;
    private lateinit var duration : EditText;
    private lateinit var chipGroup : List<Chip>;
    private var credit : Int = 0;
    private var error = "";
    private var perMonth : Int = 0;

    private lateinit var soundPool: SoundPool
    private var successSound: Int = 0;
    private var errorSound: Int = 0;
    private var buttonSound: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_rent)
        //initialize the binding that control the instrument being displayed

        instrumentImage = findViewById(R.id.imageView) // Initialize the Views in the layout
        duration = findViewById(R.id.duration)

        btn_cancel = findViewById(R.id.btn_cancel)
        btn_save = findViewById(R.id.btn_save)

        creditView = findViewById(R.id.credit)
        total_cost = findViewById(R.id.total_cost)

        chipGroup = listOf(  // Populate the chip list for accessories select
            findViewById(R.id.chip),
            findViewById(R.id.chip2),
            findViewById(R.id.chip3)
        );


        val audioAttributes = AudioAttributes.Builder() // Initialize the sound pool
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        successSound = soundPool.load(this, R.raw.success, 1) // Initialize the sound effects
        errorSound = soundPool.load(this, R.raw.error, 1)
        buttonSound = soundPool.load(this, R.raw.button, 1)

        val intent = getIntent(); // Get the intent that was used in Main Activity
        val tempInstrument = intent.getParcelableExtra("instrument", Instrument::class.java) // Get the Instrument parcelable object passed from Main Activity
        val tempCredit = intent.getIntExtra("credit", 0) // Get the current credit
        credit = tempCredit;
        creditView.setText(credit.toString());

        if(tempInstrument != null){  // Set the data binding to the selected instrument
            instrument = tempInstrument;
            perMonth = instrument.cost;
            binding.instrument = instrument;
            instrumentImage.setImageResource(instrument.image)

            Log.d("Instrument", "Setted Instrument")
        }
        else{
            Log.d("Instrument", "Null instrument")
        }

        btn_cancel.setOnClickListener(this)  // Set the on click listener event of the buttons to this class
        btn_save.setOnClickListener(this)

        duration.addTextChangedListener(object : TextWatcher {  // TextWatcher to run whenever the diration text is changed
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Duration Text", "Before changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Duration Text", "On changed")
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("Duration Text", "After changed")
                updateUI(); // Update the UI with the new duration text value
            }
        })

        for (i in 0 until chipGroup.count()) {
            chipGroup[i].setOnCheckedChangeListener { _, isChecked ->  // Set check change listener for each of the chip in the chip list
                if (isChecked) {
                    chipGroup[i].setChipBackgroundColorResource(R.color.secondary) // Set checked color
                    chipGroup[i].setTextColor(ContextCompat.getColor(this, R.color.white)); // Set checked text color
                    perMonth += instrument.accessories.values.toIntArray()[i]; // Add to cost per month
                } else {
                    chipGroup[i].setChipBackgroundColorResource(R.color.unchecked) // Set unchecked color
                    chipGroup[i].setTextColor(ContextCompat.getColor(this, R.color.textlight));// Set unckecked text color
                    perMonth -= instrument.accessories.values.toIntArray()[i]; // Reduce from cost per month
                }
                soundPool.play(buttonSound, 1f, 1f, 0, 0, 1f) // Play button sound when chip is clicked
                updateUI();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun updateUI(){  // Update UI when needed
        if(!duration.text.toString().equals("")){
            val cost = perMonth * duration.text.toString().toInt() // Recalculate the total cost
            total_cost.setText(cost.toString())
            if(cost <= credit) total_cost.setTextColor(ContextCompat.getColor(this, R.color.textsufficient)) // Set text to green if have sufficient credit
            else total_cost.setTextColor(ContextCompat.getColor(this, R.color.textinsufficient)) // Set text to red if have insufficient credit

            Log.d("Duration", "Duration not empty")
        }
        else{
            total_cost.setText(0.toString()) // Set total cost to 0 if duration is not filled
            total_cost.setTextColor(ContextCompat.getColor(this, R.color.textlight))

            Log.d("Duration", "Duration empty")
        }
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, MainActivity::class.java);
        when(v?.id){
            R.id.btn_save ->{
                if(validate()){
                    instrument.months += duration.text.toString().toInt();

                    intent.putExtra("updatedinstrument", instrument);
                        // Put the Parcelable Instrument Object to be received by Main Activity

                    intent.putExtra("message", "Successfully Booked"); // Message for snack bar in Main Activity
                    intent.putExtra("cost", perMonth * duration.text.toString().toInt()); // Put in cost to reduct in the Main Activity

                    Log.d("Validation", "Valid")

                    setResult(RESULT_OK, intent); // Set RESULT_OK and finish to return to MainActivity, triggering the getResult function
                    finish();
                }
                else{
                    showSnackBar(error, R.drawable.xmark);
                    soundPool.play(errorSound, 1f, 1f, 0, 0, 1f)

                    Log.d("Validation", "Invalid: " + error)
                }
            }
            R.id.btn_cancel ->{
                intent.putExtra("message", "Cancelled"); // Set message for snack bar in Main Activity
                setResult(RESULT_OK, intent); // Return to Main Activity
                finish();
            }
        }
    }

    fun validate() : Boolean{
        error = "";
        if(!check(duration.text.toString().equals(""), "Duration cannot be empty")){
            check(duration.text.toString().toInt() <= 0, "Duration cannot be 0")
            check(duration.text.toString().toInt() * perMonth > credit, "Insufficient credits")
        }
        //check(ratingBar.rating == 0f, "Rating cannot be empty")
        return error.equals("");
    }

    fun check(condition : Boolean, message: String) : Boolean{
        if(condition){  // Check the condition of error
            if(!error.equals("")){ // Break line if not the first error
                error += "\n"
            }
            error += "• " + message; // Add the error message
        }
        return condition
    }

    @SuppressLint("RestrictedApi", "MissingInflatedId")
    fun showSnackBar(text : String, icon : Int){
        val snackbarView = layoutInflater.inflate(R.layout.snackbar_layout, null)  // Use custom snack bar layout
        val snackbarText : TextView = snackbarView.findViewById(R.id.snackbar_text) // Get the View in the custom snack bar layout
        val snackbarIcon : ImageView = snackbarView.findViewById(R.id.snackbar_icon)
        snackbarText.text = text; //Set the text of the snack bar
        snackbarIcon.setImageResource(icon);  // Set the snack bar icon

        val snackbar = Snackbar.make(btn_cancel, "", Snackbar.LENGTH_LONG)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
        snackbarLayout.addView(snackbarView, 0)
        snackbar.show()
    }
}