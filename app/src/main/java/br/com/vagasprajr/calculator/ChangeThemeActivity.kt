package br.com.vagasprajr.calculator

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.com.vagasprajr.calculator.databinding.ActivityChangeThemeBinding
import br.com.vagasprajr.calculator.databinding.ActivityMainBinding

class ChangeThemeActivity: AppCompatActivity() {

    lateinit var switchBinding: ActivityChangeThemeBinding

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        switchBinding = ActivityChangeThemeBinding.inflate(layoutInflater)
        val view = switchBinding.root

        setContentView(view)

        switchBinding.toolbar.setNavigationOnClickListener {
            finish()
        }

        switchBinding.switchMaterial.setOnCheckedChangeListener { buttonView, isChecked ->

            sharedPreferences = getSharedPreferences("Dark Theme", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("night_mode", isChecked)
            editor.apply()

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = this.getSharedPreferences("Dark Theme", MODE_PRIVATE)
        val isDark = sharedPreferences.getBoolean("night_mode", false)
        switchBinding.switchMaterial.isChecked = isDark
    }
}
