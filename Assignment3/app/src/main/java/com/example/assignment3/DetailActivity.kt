package com.example.assignment3

import GameViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.lifecycle.ViewModelProvider

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var name : TextView;
    lateinit var rating : TextView;
    lateinit var price : TextView;
    lateinit var description : TextView;
    lateinit var cover : ImageView;
    lateinit var trailer : WebView;
    lateinit var taglist : ChipGroup;

    lateinit var game : Game;

    lateinit var gameViewModel : GameViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.detail_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        name = findViewById(R.id.name)
        rating = findViewById(R.id.rating)
        price = findViewById(R.id.price)
        description = findViewById(R.id.description)
        cover = findViewById(R.id.cover)
        trailer = findViewById(R.id.trailer)
        taglist = findViewById(R.id.taglist)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        val intent = getIntent();
        game = intent.getParcelableExtra("game", Game::class.java) ?: Game("", "", "", 0f, 0f, "", listOf());
        if(game != null){  // Set the data binding to the selected instrument
            name.setText(game.name)
            rating.setText(game.rating.toString())
            price.setText("$" + game.price.toString())
            description.setText(game.description)

            for (tag in game.tags) {
                val chip = Chip(this, null, R.style.chipStyle).apply {
                    text = tag
                    textSize = 22f
                    isClickable = true

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
                chip.setChipBackgroundColorResource(R.color.third)
                chip.setTextColor(ContextCompat.getColor(this, R.color.secondary))
                taglist.addView(chip)
            }

            Glide.with(cover)
                .load(game.coverURL)
                .into(cover)

            trailer.setWebViewClient(WebViewClient())
            // Enable JavaScript
            val webSettings: WebSettings = trailer.getSettings()
            webSettings.javaScriptEnabled = true
            // Load the YouTube video
            trailer.loadUrl("https://www.youtube.com/embed/" + game.trailerID + "?autoplay=1&loop=1&playlist=" + game.trailerID)
            val wm = getSystemService(WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val width = display.width // Get screen width


            // Set the height
            trailer.getLayoutParams().height = (width * 9 / 16)
        }

        findViewById<Button>(R.id.btn_edit).setOnClickListener(this)
        findViewById<Button>(R.id.btn_delete).setOnClickListener(this)
        findViewById<ImageView>(R.id.btn_back).setOnClickListener(this)
    }

    val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ // Function run when return from Rent Activity
        if(it.resultCode == Activity.RESULT_OK){
            val message = it.data?.getStringExtra("message")
            if(message != null){
                if(!message.equals("Cancelled")){
                    intent.putExtra("oldname", game.name)
                    game = it.data?.getParcelableExtra("updatedgame", Game::class.java) ?: Game("", "", "", 0f, 0f, "", listOf());
                    intent.putExtra("updatedgame", game)
                    setResult(RESULT_OK, intent); // Return to Main Activity
                    finish();
                }
                else{
                    showSnackBar(message, R.drawable.checkmark)  // Show snack bar of the returned message from Rent Activity
                }
            }
        }
    }

    fun deleteGame(){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref: DatabaseReference = database.getReference("Game/" + game.name)

        // Remove the key
        ref.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                intent.putExtra("message", "Deleted " + game.name); // Set message for snack bar in Main Activity
                intent.putExtra("delete", game.name);
                setResult(RESULT_OK, intent); // Return to Main Activity
                finish();
            } else {
                println("Error removing key: ${task.exception?.message}")
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_delete ->{
                showDialog()
            }
            R.id.btn_edit ->{
                val intent = Intent(this@DetailActivity, EditCreateActivity::class.java)
                intent.putExtra("game", game);
                getResult.launch(intent);
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

    private fun showDialog() {
        // Create a dialog instance
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_layout)

        // Set up the button click listener
        dialog.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            deleteGame()
            dialog.dismiss()  // Close the dialog
        }
        dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()  // Close the dialog
        }

        // Show the dialog
        dialog.show()
    }
}