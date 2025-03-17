package com.example.week9

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class MainActivity : AppCompatActivity() {
    lateinit var txt : TextView
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
        txt = findViewById(R.id.txt)
        callThread()
    }

    fun callThread(){
        CoroutineScope(IO).launch{
            /*var startTime = System.currentTimeMillis()
            var job1 = launch{
                val result = Thread1()
            }
            var job2 = launch{
                val time1 = measureTime {
                    withContext(Main){
                        txt.setText("Debug: launching thrad 2 " + Thread.currentThread().name)
                    }
                }
                val result = Thread2()
            }

            job1.invokeOnCompletion {
                Log.d("T1", "Thread 1 Completed in " + (System.currentTimeMillis() - startTime).toString())
            }*/

            /*job2.invokeOnCompletion {
                Log.d("G2", "Thread 2 Completed in " + (System.currentTimeMillis() - startTime).toString())
            }*/

            var executeTime = measureTimeMillis {
                val job1 : Deferred<String> = async{
                    Thread1()
                }
                val job2 : Deferred<String> = async{
                    Thread2()
                }
                var result1 = job1.await()
                var result2 = job2.await()
                /*var result1 = Thread1()
                var result2 = Thread2()*/
                var result = result1 + result2
                Log.d("T1", result.toString())
            }
            Log.d("T1", executeTime.toString())
        }
    }

    suspend fun Thread1(): String{
        for(i in 0..1000) Log.d("T1", "Thread 1 " + i.toString() )
        /*Log.d("T1", "Start Thread 1")
        delay(1000)
        Log.d("T1", "Start Thread 1")*/
        return "Thread 1"
    }

    suspend fun Thread2(): String{
        for(i in 0..1000) Log.d("T2", "Thread 2 " + i.toString() )
        /*Log.d("T2", "Start Thread 2")
        delay(1700)
        Log.d("T2", "Start Thread 2")*/
        return "Thread 1"
    }
}