package com.example.assignment3

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class EditCreateActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var name : EditText;
    lateinit var coverURL : EditText;
    lateinit var price : EditText;
    lateinit var trailer : EditText;
    lateinit var rating : EditText;
    lateinit var description : EditText;
    lateinit var cover : ImageView;
    lateinit var taglist : ChipGroup;

    lateinit var game : Game;

    val tags = listOf(
        "Action",
        "Adventure",
        "RPG",
        "Strategy",
        "Simulation",
        "Puzzle",
        "Multiplayer",
        "Shooter",
        "Platformer",
        "Survival",
        "Stealth",
        "Sandbox",
        "Casual"
    )
    var selectedtags = mutableListOf<String>()
    private val PICK_IMAGE_REQUEST = 1

    lateinit var imageUri : Uri

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.editcreate_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.name)
        coverURL = findViewById(R.id.coverURL)
        rating = findViewById(R.id.rating)
        price = findViewById(R.id.price)
        description = findViewById(R.id.description)
        cover = findViewById(R.id.cover)
        trailer = findViewById(R.id.trailer)
        taglist = findViewById(R.id.taglist)

        val intent = getIntent();
        game = intent.getParcelableExtra("game", Game::class.java) ?: Game("", "", "", 0f, 0f, "", listOf());
        if(game.name != ""){  // Set the data binding to the selected instrument
            name.setText(game.name)
            coverURL.setText(game.coverURL)
            price.setText(game.price.toString())
            trailer.setText(game.trailerID)
            rating.setText(game.rating.toString())
            description.setText(game.description)
            findViewById<TextView>(R.id.mode).setText("Edit")
            selectedtags = game.tags.toMutableList()
        }

        val chipGroup = findViewById<ChipGroup>(R.id.taglist)
        for (tag in tags) {
            val chip = Chip(this, null, R.style.chipStyle).apply {
                text = tag
                textSize = 22f
                isClickable = true
                isCheckable = true

                val heightInDp = 52
                val heightInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    heightInDp.toFloat(),
                    resources.displayMetrics
                ).toInt()

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                    heightInPx // Height in pixels (or use dp)
                )

                typeface = Typeface.defaultFromStyle(Typeface.BOLD)

                val cornerInDp = 10
                val cornerInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    cornerInDp.toFloat(),
                    resources.displayMetrics
                ).toFloat() // Set your desired corner radius
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(cornerInPx) // Set corner radius for all corners
                    .build()
            }
            chip.setChipBackgroundColorResource(R.color.unchecked)
            chip.setTextColor(ContextCompat.getColor(this, R.color.textlight))
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    chip.setChipBackgroundColorResource(R.color.secondary) // Set checked color
                    chip.setTextColor(ContextCompat.getColor(this, R.color.white)); // Set checked text color
                    if(!selectedtags.contains(tag)) selectedtags.add(tag)
                } else {
                    chip.setChipBackgroundColorResource(R.color.unchecked) // Set unchecked color
                    chip.setTextColor(ContextCompat.getColor(this, R.color.textlight));// Set unckecked text color
                    selectedtags = selectedtags.filter { it != tag }.toMutableList()
                }
                /*soundPool.play(buttonSound, 1f, 1f, 0, 0, 1f)
                updateUI();*/
            }
            chip.isChecked = selectedtags.contains(tag);
            chipGroup.addView(chip)
        }

        findViewById<Button>(R.id.btn_cancel).setOnClickListener(this)
        findViewById<Button>(R.id.btn_delete).setOnClickListener(this)
        findViewById<ImageView>(R.id.btn_back).setOnClickListener(this)

        coverURL.addTextChangedListener(object : TextWatcher {  // TextWatcher to run whenever the diration text is changed
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Cover Text", "Before changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Cover Text", "On changed")
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("Cover Text", "After changed")
                Glide.with(cover)
                    .load(coverURL.text.toString())
                    .into(cover)
                 // Update the UI with the new duration text value
            }
        })
    }


    fun saveGame(){
        val uniqueKey = System.currentTimeMillis().toString()
        val database: DatabaseReference = FirebaseDatabase.getInstance().reference
        val table = database.child("Game")
        if(game.name.equals("")){
            game.name = name.text.toString();
            game.coverURL = coverURL.text.toString();
            game.price = price.text.toString().toFloat();
            game.trailerID = trailer.text.toString();
            game.rating = rating.text.toString().toFloat();
            game.description = description.text.toString();
            game.tags = selectedtags;
            table.child(game.name).setValue(game).addOnSuccessListener {
                intent.putExtra("message", "New Game Saved"); // Set message for snack bar in Main Activity
                intent.putExtra("newgame", game)
                setResult(RESULT_OK, intent); // Return to Main Activity
                finish();
            }
        }
        else{
            table.child(game.name).removeValue().addOnSuccessListener {
                game.name = name.text.toString();
                game.coverURL = coverURL.text.toString();
                game.price = price.text.toString().toFloat();
                game.trailerID = trailer.text.toString();
                game.rating = rating.text.toString().toFloat();
                game.description = description.text.toString();
                game.tags = selectedtags;
                table.child(game.name).setValue(game).addOnSuccessListener {
                    intent.putExtra("message", "Game updated"); // Set message for snack bar in Main Activity
                    intent.putExtra("updatedgame", game)
                    setResult(RESULT_OK, intent); // Return to Main Activity
                    finish();
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_delete ->{
                saveGame()
            }
            R.id.btn_cancel ->{
                intent.putExtra("message", "Cancelled"); // Set message for snack bar in Main Activity
                setResult(RESULT_OK, intent); // Return to Main Activity
                finish();
            }
            R.id.btn_back ->{
                setResult(RESULT_OK, intent); // Return to Main Activity
                finish();
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

        val snackbar = Snackbar.make(findViewById(R.id.name), "", Snackbar.LENGTH_LONG)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
        snackbarLayout.addView(snackbarView, 0)
        snackbar.show()  // Show the snack bar
    }


}