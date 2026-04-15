package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val title = intent.getStringExtra("EVENT_TITLE")
        val seats = intent.getIntExtra("SEATS", 0)
        val price = intent.getDoubleExtra("PRICE", 0.0)

        findViewById<TextView>(R.id.tvConfirmationDetails).text = 
            "Event: $title\nSeats: $seats\nTotal: $${String.format("%.2f", price)}"

        findViewById<Button>(R.id.btnBackToHome).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
