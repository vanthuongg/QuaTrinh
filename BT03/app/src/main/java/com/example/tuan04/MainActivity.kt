package com.example.tuan04

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var rootView: ConstraintLayout
    private lateinit var switchToggle: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rootView = findViewById(R.id.main)
        switchToggle = findViewById(R.id.switchToggle)

        switchToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onSwitchTurnedOn()
            } else {
                onSwitchTurnedOff()
            }
        }
    }

    private fun onSwitchTurnedOn() {
        changeBackground()
    }

    private fun onSwitchTurnedOff() {
        rootView.setBackgroundResource(android.R.color.white)
    }

    private fun changeBackground() {
        val backgrounds = listOf(
            R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg3,
            R.drawable.bg4
        )

        val randomBackground = backgrounds[Random.nextInt(backgrounds.size)]
        val drawable: Drawable? = ContextCompat.getDrawable(this, randomBackground)
        rootView.background = drawable
    }
}
