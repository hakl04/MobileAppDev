package com.example.assignment3

import GameViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import io.ktor.client.HttpClient

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import io.ktor.client.call.* // For `body`
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.util.InternalAPI
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var search : EditText
    lateinit var spinner : ProgressBar

    lateinit var games : RecyclerView

    lateinit var gameViewModel : GameViewModel

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
    val selectedtags = mutableListOf<String>()
    val database = FirebaseDatabase.getInstance().reference
    lateinit var gamelist : ArrayList<Game>

    @OptIn(InternalAPI::class)
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
        search = findViewById(R.id.search)
        spinner = findViewById(R.id.progressBar)
        val searchIcon = findViewById<ImageView>(R.id.searchIcon)
        val addButon = findViewById<ImageView>(R.id.btn_add)
        searchIcon.setOnClickListener(this)
        addButon.setOnClickListener(this)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

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
                    selectedtags.add(tag)
                } else {
                    chip.setChipBackgroundColorResource(R.color.unchecked) // Set unchecked color
                    chip.setTextColor(ContextCompat.getColor(this, R.color.textlight));// Set unckecked text color
                    selectedtags.remove(tag)
                }
                getGames(search.text.toString(), selectedtags)
                /*soundPool.play(buttonSound, 1f, 1f, 0, 0, 1f)
                updateUI();*/
            }
            chipGroup.addView(chip)
        }

        games = findViewById(R.id.games);

        games.layoutManager = LinearLayoutManager(this)
        games.setHasFixedSize(false);
        /*for(i in 0 until studentList.size){
            data.add(DataClass(studentList[i], imageList[i]))
        }*/

        gameViewModel.gamelist.observe(this, Observer { gl ->
            val adapter = RecyclerViewAdapter(gl)
            games.setAdapter(adapter);
        })


        gamelist = ArrayList<Game>()

        FirebaseApp.initializeApp(this)

        getGames("", listOf())

        var data = ArrayList<Game>()
        data.add(Game("Grand Theft Auto V", "https://image.api.playstation.com/vulcan/ap/rnd/202101/2019/JdvqcPlTYMxXrA1QQJm6TbDX.png", "QkkoHAzjnUs", 59.99f, 4.39f, "Experience an expansive open world filled with crime, heists, and a rich storyline as you navigate the lives of three unique characters in Los Santos.", listOf("Action", "Adventure")))
        data.add(Game("Elden Ring", "https://cdn.max-c.com/wiki/1245620/Elden-Ring-Icon.jpg", "AKXiKBnzpBQ", 39.99f, 4.71f, "Explore a beautifully crafted fantasy world filled with challenging enemies and deep lore, where your choices shape the destiny of the realm.", listOf("Action", "RPG")))
        data.add(Game("Kingdom Come Deliverance", "https://cdn.mobygames.com/covers/2776348-kingdom-come-deliverance-playstation-4-front-cover.png", "tpnuBdG9txM", 34.99f, 4.05f, "Immerse yourself in a historically accurate medieval open-world RPG, where you play as a young blacksmith seeking revenge amid a civil war in Bohemia.", listOf("Adventure", "RPG")))
        data.add(Game("The Elder Scroll V: Skyrim", "https://www.fastgames.dk/wp-content/uploads/2022/03/The-Elder-Scrolls-V-Skyrim.jpg", "6umhTJQltak", 21.99f, 3.83f, "Step into the role of the Dragonborn and embark on a quest to save the world from dragons, exploring vast landscapes and engaging in epic battles.", listOf("RPG", "Adventure")))
        data.add(Game("Cyber Punk 2077", "https://cdn.mobygames.com/covers/10676751-cyberpunk-2077-playstation-4-front-cover.jpg", "LembwKDo1Dk", 6.99f, 3.65f, "Dive into a dystopian future filled with technology, crime, and corporate intrigue, where you create your own character and shape your destiny in Night City.", listOf("Action", "Adventure")))
        data.add(Game("Sekiro: Shadow Die Twice", "https://cdn.mobygames.com/covers/7783539-sekiro-shadows-die-twice-playstation-4-front-cover.jpg", "4OgoTZXPACo", 0.99f, 2.67f, "Engage in intense swordplay and stealth as a shinobi on a quest for revenge in a reimagined late 1500s Sengoku period Japan.", listOf("Action", "Adventure")))
        data.add(Game("Hollow Knight", "https://cdn.mobygames.com/covers/3632091-hollow-knight-nintendo-switch-front-cover.jpg", "UAO2urG23S4", 19.99f, 4.99f, "Explore a beautifully hand-drawn world of insects and heroes in this challenging platformer, filled with hidden secrets and fierce battles.", listOf("Action", "Platformer", "Adventure")))
        data.add(Game("Crusader Kings III", "https://korobok.store/upload/iblock/65b/rfb0ugq054vd08u6v9kg1n73s3x19kbg.png", "Demi3MfHHYw", 49.99f, 4.20f, "Manage a medieval dynasty and navigate complex political intrigue, where your choices can lead to glory or ruin in this grand strategy game.", listOf("Strategy")))
        data.add(Game("Stardew Valley", "https://cdn.mobygames.com/covers/7453765-stardew-valley-playstation-4-front-cover.jpg", "ot7uXNQskhs", 39.99f, 4.01f, "Build your dream farm in a charming pixelated world, cultivating crops, raising animals, and forming relationships with the townsfolk.", listOf("Casual")))
        data.add(Game("Minecraft", "https://cdn.mobygames.com/covers/9261167-minecraft-playstation-4-front-cover.jpg", "MmB9b5njVbA", 6.99f, 3.95f, "Unleash your creativity in this sandbox game where you can build, explore, and survive in a blocky, procedurally generated world.", listOf("Simulation", "Sandbox", "Survival")))
        data.add(Game("Total War: Shogun 2", "https://assets-prd.ignimgs.com/2022/04/18/shogun2-1650241291767.jpg", "tQPLUyzVC9E", 19.99f, 3.77f, "Command armies and strategize to dominate feudal Japan, blending turn-based strategy with real-time battles in this critically acclaimed title.", listOf("Strategy")))
        data.add(Game("Call Of Duty: Modern Warfare", "https://cdn.mobygames.com/covers/8184635-call-of-duty-modern-warfare-playstation-4-front-cover.jpg", "bH1lHCirCGI", 19.99f, 3.23f, "Experience intense military action in this modern shooter, featuring a gripping campaign and competitive multiplayer modes.", listOf("Action", "Shooter", "Multiplayer")))
        data.add(Game("Astro Bot", "https://roadtovrlive-5ea0.kxcdn.com/wp-content/uploads/2018/11/astro-bot-box-logo.jpg", "wHMNQzLG_Jg", 29.99f, 4.87f, "Embark on a whimsical adventure in this VR platformer, helping your robot friends through creative levels filled with challenges and surprises.", listOf("Casual", "Adventure")))
        data.add(Game("Black Myth Wukong", "https://image.api.playstation.com/vulcan/ap/rnd/202405/2117/bd406f42e9352fdb398efcf21a4ffe575b2306ac40089d21.png", "pnSsgRJmsCc", 34.99f, 4.93f, "Follow the journey of the Monkey King in this action-adventure game inspired by Chinese mythology, featuring breathtaking visuals and fluid combat.", listOf("Adventure", "Action")))
        data.add(Game("Portal 2", "https://vignette.wikia.nocookie.net/half-life/images/8/89/Portal_2_cover.jpg/revision/latest/top-crop/width/480/height/480?cb=20110102221935&path-prefix=en", "tax4e4hBBZc", 24.99f, 4.24f, "Solve mind-bending puzzles using a portal gun in this critically acclaimed puzzle-platformer, where clever humor and challenging gameplay await.", listOf("Puzzle", "Adventure")))
        data.add(Game("Baldur Gate III", "https://cdn.mobygames.com/covers/17586294-baldurs-gate-iii-digital-deluxe-edition-playstation-5-front-cove.png", "1T22wNvoNiU", 59.99f, 4.56f, "Enter a world of fantasy and danger in this RPG, where your decisions and character customization create a unique story and immersive experience.", listOf("RPG")))
        data.add(Game("Totally Accurate Battle Simulator", "https://cdn.mobygames.com/covers/11115545-totally-accurate-battle-simulator-nintendo-switch-front-cover.jpg", "Z2e9vd3Znz4", 19.99f, 3.19f, "Enjoy whimsical, physics-based battles where you can pit various units against each other in a hilarious and chaotic simulation.", listOf("Simulation", "Strategy", "Sandbox")))
        data.add(Game("Assassin's Creed: Shadow", "https://assets-prd.ignimgs.com/2024/05/15/acshadows-1715789601294.jpg", "zxtJFdnMo7k", 59.99f, 1.87f, "Sneak through a beautifully rendered world in this stealth action RPG, where you unravel a gripping story of betrayal and vengeance.", listOf("Stealth", "Action", "RPG")))


        val table = database.child("Game")
        for(game in data){
            table.child(game.name).setValue(game).addOnSuccessListener {
                //Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                //Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
            }.addOnCanceledListener {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true // Indicates a single tap
            }
        })

        games.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
                val childView = recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)
                if (childView != null && gestureDetector.onTouchEvent(motionEvent)) {
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        val position = recyclerView.getChildAdapterPosition(childView)
                        if (position != RecyclerView.NO_POSITION) {
                            val clickedGame = gameViewModel.gamelist.value!![position]
                            // Handle the click event
                            //Toast.makeText(this@MainActivity, "Clicked: ${clickedGame.name}", Toast.LENGTH_SHORT).show()
                            // You can also start a new Activity or perform other actions here
                            val intent = Intent(this@MainActivity, DetailActivity::class.java)
                            intent.putExtra("game", clickedGame);
                            getResult.launch(intent);
                        }
                    }
                }
                return super.onInterceptTouchEvent(recyclerView, motionEvent)
            }
        })

        //getRating()

    }

    val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ // Function run when return from Rent Activity
        if(it.resultCode == Activity.RESULT_OK){
            val message = it.data?.getStringExtra("message")
            if(message != null){
                showSnackBar(message, R.drawable.checkmark)  // Show snack bar of the returned message from Rent Activity
            }
            val delete = it.data?.getStringExtra("delete")
            if(delete != null){
                gameViewModel.remove(delete);  // Show snack bar of the returned message from Rent Activity
            }
            val updatedgame = it.data?.getParcelableExtra("updatedgame", Game::class.java) ?: Game("", "", "", 0f, 0f, "", listOf());
            if(!updatedgame.name.equals("")){
                gameViewModel.update(updatedgame, it.data?.getStringExtra("oldname") ?: "");
            }
            val newgame = it.data?.getParcelableExtra("newgame", Game::class.java) ?: Game("", "", "", 0f, 0f, "", listOf());
            if(!newgame.name.equals("")){
                gameViewModel.add(newgame);
            }
        }
    }

    @OptIn(InternalAPI::class)
    fun getRating(){
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }) // Configure JSON serialization
            }
        }

        lifecycleScope.launch {
            try {
                val response: HttpResponse = client.post("https://api.igdb.com/v4/age_ratings") {
                    headers {
                        append("Client-ID", "Your_Client_ID")
                        append("Authorization", "Bearer Your_Access_Token")
                        append("Accept", "application/json")
                    }
                    body = "fields rating;"
                }

                Toast.makeText(this@MainActivity, "Game Rating: $response", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
            }
            finally {
                client.close()
            }
        }
        // Making a POST request
        // Print the response body

    }

    fun getGames(search : String, tagfilter : List<String>){
        /*val adapter = RecyclerViewAdapter(gamelist)
        games.setAdapter(adapter);*/
        var found = false;
        spinner.visibility = View.VISIBLE
        games.visibility = View.GONE
        database.child("Game").get().addOnCompleteListener { task ->
            spinner.visibility = View.GONE
            games.visibility = View.VISIBLE
            gamelist.clear()
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    val stringList = mutableListOf<String>()
                    for (it in dataSnapshot.children) {
                        val name = it.child("name").getValue(String::class.java) ?: ""
                        if(name.toLowerCase().contains(search.toLowerCase()) || search.equals("")){
                            stringList.clear()
                            var matchtag = selectedtags.isEmpty()
                            for (childSnapshot in it.child("tags").children) {
                                val value = childSnapshot.getValue(String::class.java)
                                if(selectedtags.contains(value)){
                                    matchtag = true;
                                }
                                if (value != null) {
                                    stringList.add(value)
                                }
                            }
                            if(matchtag){
                                found = true;
                                gamelist.add(Game(name, it.child("coverURL").getValue(String::class.java) ?: "", it.child("trailerID").getValue(String::class.java) ?: "", it.child("price").getValue(Float::class.java) ?: 0.0f, it.child("rating").getValue(Float::class.java) ?: 0.0f, it.child("description").getValue(String::class.java) ?: "", ArrayList(stringList)))
                            }
                        }
                    }
                    gameViewModel.set(gamelist);
                } else {
                }
            } else {
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.searchIcon ->{                   // Switch to the next instrumnt in the list
                getGames(search.text.toString(), selectedtags)
            }
            R.id.btn_add ->{                   // Switch to the next instrumnt in the list
                val intent = Intent(this@MainActivity, EditCreateActivity::class.java)
                getResult.launch(intent);
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

        val snackbar = Snackbar.make(spinner, "", Snackbar.LENGTH_LONG)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0) // Remove default padding
        snackbarLayout.addView(snackbarView, 0)
        snackbar.show()  // Show the snack bar
    }
}
