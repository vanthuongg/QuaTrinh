package com.example.tuan04

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginMainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val switchToRegister = findViewById<TextView>(R.id.switchToRegister)

        switchToRegister.setOnClickListener {
            val intent = Intent(this, RegisterMainActivity::class.java)
            startActivity(intent)
        }
    }
}