package au.edu.swin.sdmd.w03_calculations

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val number1 = findViewById<EditText>(R.id.number1)
        val number2 = findViewById<EditText>(R.id.number2)
        val add = findViewById<RadioButton>(R.id.add)
        val sub = findViewById<RadioButton>(R.id.sub)
        val mul = findViewById<RadioButton>(R.id.mul)
        val div = findViewById<RadioButton>(R.id.div)
        add.setText(R.string.add)
        sub.setText(R.string.sub)
        mul.setText(R.string.mul)
        div.setText(R.string.div)
        val operators = findViewById<RadioGroup>(R.id.operator)
        val answer : TextView = findViewById(R.id.answer)

        val equals = findViewById<Button>(R.id.equals)
        equals.setText(R.string.equals);
        equals.setOnClickListener {
            val result = when(operators.checkedRadioButtonId){
                R.id.add -> add(number1.text.toString().toFloat(), number2.text.toString().toFloat()).toString();
                R.id.sub -> sub(number1.text.toString().toFloat(), number2.text.toString().toFloat()).toString();
                R.id.mul -> mul(number1.text.toString().toFloat(), number2.text.toString().toFloat()).toString();
                R.id.div -> div(number1.text.toString().toFloat(), number2.text.toString().toFloat()).toString();
                else -> ""
            }
            answer.setText(result);
            // TODO: show result on the screen
        }
    }

    // adds two numbers together
    private fun add(number1: Float, number2: Float) = number1 + number2
    private fun sub(number1: Float, number2: Float) = number1 - number2
    private fun mul(number1: Float, number2: Float) = number1 * number2
    private fun div(number1: Float, number2: Float) = number1 / number2


}