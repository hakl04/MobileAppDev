package com.example.assignment2


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    lateinit var instrument : Instrument;
    lateinit var creditView : TextView;
    lateinit var total_cost : TextView;
    lateinit var btn_cancel : Button;
    lateinit var btn_save : Button;
    lateinit var binding : ActivityRentBinding
    lateinit var instrumentImage : ImageView;
    lateinit var duration : EditText;
    lateinit var chipGroup : List<Chip>;
    lateinit var ratingBar : RatingBar;
    var credit : Int = 0;
    var error = "";
    var perMonth : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*setContentView(R.layout.activity_rent)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rent)
        instrumentImage = findViewById(R.id.imageView)
        ratingBar = findViewById(R.id.ratingBar)
        duration = findViewById(R.id.duration)

        btn_cancel = findViewById(R.id.btn_cancel)
        btn_save = findViewById(R.id.btn_save)

        creditView = findViewById(R.id.credit)
        total_cost = findViewById(R.id.total_cost)

        chipGroup = listOf(
            findViewById(R.id.chip),
            findViewById(R.id.chip2),
            findViewById(R.id.chip3)
        );

        val stars = binding.ratingBar.progressDrawable as LayerDrawable
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.star), PorterDuff.Mode.SRC_ATOP)

        val intent = getIntent();
        val tempInstrument = intent.getParcelableExtra("instrument", Instrument::class.java)
        val tempCredit = intent.getIntExtra("credit", 0)
        credit = tempCredit;
        creditView.setText(credit.toString());
        if(tempInstrument != null){
            instrument = tempInstrument;
            perMonth = instrument.cost;
            binding.instrument = instrument;
            instrumentImage.setImageResource(instrument.image)
        }
        else{
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }

        btn_cancel.setOnClickListener(this)
        btn_save.setOnClickListener(this)

        duration.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // You can handle logic before text changes if needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Call your function here when the text changes
            }

            override fun afterTextChanged(s: Editable?) {
                updateUI();
                // Handle logic after the text has changed if needed
            }
        })

        for (i in 0 until chipGroup.count()) {
            chipGroup[i].setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    chipGroup[i].setChipBackgroundColorResource(R.color.secondary) // Set checked color
                    chipGroup[i].setTextColor(ContextCompat.getColor(this, R.color.white));
                    perMonth += instrument.accessories.values.toIntArray()[i];
                } else {
                    chipGroup[i].setChipBackgroundColorResource(R.color.unchecked) // Set unchecked color
                    chipGroup[i].setTextColor(ContextCompat.getColor(this, R.color.textlight));
                    perMonth -= instrument.accessories.values.toIntArray()[i];
                }
                updateUI();
            }
        }
    }

    fun updateUI(){
        if(!duration.text.toString().equals("")){
            total_cost.setText((perMonth * duration.text.toString().toInt()).toString())
        }
        else{
            total_cost.setText(0.toString())
        }
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, MainActivity::class.java);
        when(v?.id){
            R.id.btn_save ->{
                if(validate()){
                    instrument.rating = ratingBar.rating;
                    instrument.months = duration.text.toString().toInt();
                    intent.putExtra("updatedinstrument", instrument);
                    intent.putExtra("message", "Successfully Booked");
                    intent.putExtra("cost", perMonth * duration.text.toString().toInt());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else{
                    showSnackBar(error, R.drawable.xmark);
                }
            }
            R.id.btn_cancel ->{
                intent.putExtra("message", "Cancelled");
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    fun validate() : Boolean{
        error = "";
        if(!check(duration.text.toString().equals(""), "Duration cannot be empty")){
            check(duration.text.toString().toInt() <= 0, "Duration cannot be 0")
            check(duration.text.toString().toInt() * instrument.cost > credit, "Insufficient credits")
        }
        check(ratingBar.rating == 0f, "Rating cannot be empty")
        return error.equals("");
    }

    fun check(condition : Boolean, message: String) : Boolean{
        if(condition){
            if(!error.equals("")){
                error += "\n"
            }
            error += "• " + message;
        }
        return condition
    }

    @SuppressLint("RestrictedApi", "MissingInflatedId")
    fun showSnackBar(text : String, icon : Int){
        val snackbarView = layoutInflater.inflate(R.layout.snackbar_layout, null)
        val snackbarText : TextView = snackbarView.findViewById(R.id.snackbar_text)
        val snackbarIcon : ImageView = snackbarView.findViewById(R.id.snackbar_icon)
        snackbarText.text = text;
        snackbarIcon.setImageResource(icon);

        val snackbar = Snackbar.make(btn_cancel, "", Snackbar.LENGTH_LONG)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
        snackbarLayout.addView(snackbarView, 0)
        snackbar.show()
    }
}