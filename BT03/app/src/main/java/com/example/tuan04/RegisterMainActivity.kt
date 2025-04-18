package com.example.tuan04

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegisterMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val switchToLogin = findViewById<TextView>(R.id.switchToLogin)

        switchToLogin.setOnClickListener {
            val intent = Intent(this, LoginMainActivity::class.java)
            startActivity(intent)
        }
    }
}