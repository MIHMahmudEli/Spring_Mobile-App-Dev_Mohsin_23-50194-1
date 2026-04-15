package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.universityeventapp.model.Event

class EventDetailActivity : AppCompatActivity() {

    private var event: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        event = intent.getSerializableExtra("EVENT_DATA") as? Event

        setupView()
        
        findViewById<Button>(R.id.btnRegisterDetail).setOnClickListener {
            val intent = Intent(this, SeatBookingActivity::class.java)
            intent.putExtra("EVENT_DATA", event)
            startActivity(intent)
        }
    }

    private fun setupView() {
        event?.let {
            findViewById<TextView>(R.id.tvDetailTitle).text = it.title
            findViewById<TextView>(R.id.tvDetailDate).text = "${it.date} • ${it.time}"
            findViewById<TextView>(R.id.tvDetailVenue).text = it.venue
            findViewById<TextView>(R.id.tvDetailDescription).text = it.description
            findViewById<TextView>(R.id.tvStickyPrice).text = if (it.price == 0.0) "Free" else "$${String.format("%.2f", it.price)}"
            findViewById<TextView>(R.id.tvSpeakerName).text = it.speakerName
            findViewById<TextView>(R.id.tvSpeakerDesignation).text = it.speakerDesignation
            
            startCountdown()
        }
        
        // Proper back navigation
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun startCountdown() {
        val tvCountdown = findViewById<TextView>(R.id.tvCountdown)
        object : android.os.CountDownTimer(86400000, 1000) { // 24 hours fake countdown
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                tvCountdown.text = String.format("Starts in: %02d:%02d:%02d", hours, minutes, seconds)
            }
            override fun onFinish() {
                tvCountdown.text = "Event Started!"
            }
        }.start()
    }
}
