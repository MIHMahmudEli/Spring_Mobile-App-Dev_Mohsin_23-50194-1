package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.universityeventapp.model.Event

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnBrowse).setOnClickListener {
            startActivity(Intent(this, EventListActivity::class.java))
        }

        findViewById<Button>(R.id.btnRegisterFeatured).setOnClickListener {
            // Open detail for a featured event
            val featuredEvent = getFeaturedEvent()
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("EVENT_DATA", featuredEvent)
            startActivity(intent)
        }
    }

    private fun getFeaturedEvent(): Event {
        return Event(
            id = 1,
            title = "Annual Tech Symposium 2026",
            date = "OCT 24, 2026",
            time = "10:00 AM",
            venue = "Main Auditorium",
            category = "Tech",
            description = "Experience the latest in tech at our annual symposium. Featuring keynote speakers from top tech giants, hands-on workshops, and networking sessions.",
            price = 25.0,
            totalSeats = 100,
            availableSeats = 45,
            imageRes = 0
        )
    }
}