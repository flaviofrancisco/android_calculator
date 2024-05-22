package br.com.vagasprajr.calculator

import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.vagasprajr.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var number: String? = null

    var firstNumber: Double = 0.0
    var lastNumber: Double = 0.0

    var status: String? = null
    var operator: Boolean = false

    lateinit var mainBinding: ActivityMainBinding
    val formatter = DecimalFormat("#####.#####")

    var histroy: String = ""
    var currentResult : String = ""

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.textViewResult.text = "0"

        mainBinding.btnZero.setOnClickListener {
            onNumberClick("0")
        }

        mainBinding.btnOne.setOnClickListener {
            onNumberClick("1")
        }

        mainBinding.btnTwo.setOnClickListener {
            onNumberClick("2")
        }

        mainBinding.btnThree.setOnClickListener {
            onNumberClick("3")
        }

        mainBinding.btnFour.setOnClickListener {
            onNumberClick("4")
        }

        mainBinding.btnFive.setOnClickListener {
            onNumberClick("5")
        }

        mainBinding.btnSix.setOnClickListener {
            onNumberClick("6")
        }

        mainBinding.btnSeven.setOnClickListener {
            onNumberClick("7")
        }

        mainBinding.btnEight.setOnClickListener {
            onNumberClick("8")
        }

        mainBinding.btnNine.setOnClickListener {
            onNumberClick("9")
        }

        mainBinding.btnAC.setOnClickListener {
            onButtonAllClearClicked()
        }

        mainBinding.btnDel.setOnClickListener {
            number?.let {
                if (it.length == 1) {
                    onButtonAllClearClicked()
                } else {
                    number = it.substring(0, it.length -1)
                    mainBinding.textViewResult.text = number
                }
            }
        }

        mainBinding.btnEquals.setOnClickListener {
            setOperation(null)
            mainBinding.textViewHistory.text = histroy.plus(currentResult).plus("=").plus(mainBinding.textViewResult.text.toString())
            operator = false
        }

        mainBinding.btnPlus.setOnClickListener {
            setOperation("addition")
        }
        mainBinding.btnMinus.setOnClickListener { setOperation("subtraction") }
        mainBinding.btnMultiply.setOnClickListener { setOperation("multiplication") }
        mainBinding.btnDivide.setOnClickListener { setOperation("division") }

        mainBinding.btnDot.setOnClickListener {

            number = if (number == null) {
                "0."
            } else if (!number!!.contains(".")) {
                "$number."
            } else {
                number
            }

            mainBinding.textViewResult.text = number
        }

        mainBinding.toolbar.setOnMenuItemClickListener {item ->
                when(item.itemId) {
                    R.id.settings_item -> {
                        val intent = Intent(this@MainActivity, ChangeThemeActivity::class.java)
                        startActivity(intent)
                        return@setOnMenuItemClickListener true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
    }

    override  fun onPause() {
        super.onPause()

        sharedPreferences = this.getSharedPreferences("calculator_state", MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        editor.putString("number", number)
        editor.putString("status", status)
        editor.putFloat("firstNumber", firstNumber.toFloat())
        editor.putFloat("lastNumber", lastNumber.toFloat())
        editor.putBoolean("operator", operator)
        editor.putString("histroy", histroy)
        editor.putString("currentResult", currentResult)
        editor.putString("result", mainBinding.textViewResult.text.toString())
        editor.putString("view_history", mainBinding.textViewHistory.text.toString())
        editor.apply()
    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = getSharedPreferences("Dark Theme", MODE_PRIVATE)
        val isDarkTheme = sharedPreferences.getBoolean("night_mode", false)

        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = this.getSharedPreferences("calculator_state", MODE_PRIVATE)
        number = sharedPreferences.getString("number", null)
        status = sharedPreferences.getString("status", null)
        firstNumber = sharedPreferences.getFloat("firstNumber", 0.0f).toDouble()
        lastNumber = sharedPreferences.getFloat("lastNumber", 0.0f).toDouble()
        operator = sharedPreferences.getBoolean("operator", false)
        histroy = sharedPreferences.getString("histroy", "").toString()
        currentResult = sharedPreferences.getString("currentResult", "").toString()
        mainBinding.textViewResult.text = sharedPreferences.getString("result", "0")
        mainBinding.textViewHistory.text = sharedPreferences.getString("view_history", "")
    }

    private fun setOperation(operationName:String?) {

        histroy = mainBinding.textViewHistory.text.toString()
        currentResult = mainBinding.textViewResult.text.toString()

        var operatorSign = ""

        when(operationName) {
            "multiplication" -> operatorSign = "*"
            "division" ->operatorSign = "/"
            "subtraction" ->operatorSign = "-"
            "addition" ->operatorSign = "+"
        }

        if (operator) {
            when(status) {
                "multiplication" -> {
                    multiply()
                }
                "division" -> {
                    divide()
                }
                "subtraction" -> {
                    minus()
                }
                "addition" -> {
                    plus()
                }
                else-> firstNumber = mainBinding.textViewResult.text.toString().toDouble()
            }
        }

        status = operationName
        mainBinding.textViewHistory.text = histroy.plus(currentResult).plus(operatorSign)
        operator = false
        number = null
    }

    fun onNumberClick(clickedNumber: String) {
        if (number == null) {
            number = clickedNumber
        } else {
            number += clickedNumber
        }
        mainBinding.textViewResult.text = number
        operator = true
    }

    fun plus() {
        lastNumber =mainBinding.textViewResult.text.toString().toDouble()
        firstNumber += lastNumber
        mainBinding.textViewResult.text = formatter.format(firstNumber)
    }

    fun minus() {
        lastNumber =mainBinding.textViewResult.text.toString().toDouble()
        firstNumber -= lastNumber
        mainBinding.textViewResult.text = formatter.format(firstNumber)
    }

    fun multiply() {
        lastNumber =mainBinding.textViewResult.text.toString().toDouble()
        firstNumber *= lastNumber
        mainBinding.textViewResult.text = formatter.format(firstNumber)
    }

    fun divide() {
        lastNumber =mainBinding.textViewResult.text.toString().toDouble()
        if (lastNumber == 0.0) {
            val toast = Toast.makeText(applicationContext, "The divisor cannot be zero", Toast.LENGTH_SHORT)
            toast.show()
        }
        firstNumber /= lastNumber
        mainBinding.textViewResult.text = formatter.format(firstNumber)
    }

    private fun onButtonAllClearClicked() {
        number = null
        status = null
        mainBinding.textViewResult.text = "0"
        mainBinding.textViewHistory.text = ""
        firstNumber = 0.0
        lastNumber = 0.0
    }
}