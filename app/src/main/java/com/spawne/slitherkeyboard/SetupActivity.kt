package com.spawne.slitherkeyboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 96, 48, 48)
        }

        val message = TextView(this).apply {
            text = getString(R.string.setup_message)
            textSize = 16f
        }

        val enableButton = Button(this).apply {
            text = getString(R.string.enable_button)
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            }
        }

        val chooseButton = Button(this).apply {
            text = getString(R.string.choose_button)
            setOnClickListener {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showInputMethodPicker()
            }
        }

        root.addView(message)
        root.addView(enableButton)
        root.addView(chooseButton)
        setContentView(root)
    }
}
