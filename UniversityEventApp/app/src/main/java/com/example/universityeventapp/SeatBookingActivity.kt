package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.universityeventapp.adapter.SeatAdapter
import com.example.universityeventapp.model.Event
import kotlin.random.Random

class SeatBookingActivity : AppCompatActivity() {

    private lateinit var seatAdapter: SeatAdapter
    private val seats = mutableListOf<Int>()
    private var selectedCount = 0
    private var event: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_booking)

        event = intent.getSerializableExtra("EVENT_DATA") as? Event
        
        setupSeats()
        setupGridView()
        setupListeners()
    }

    private fun setupSeats() {
        // 48 seats. Mark some as booked.
        for (i in 0 until 48) {
            val isBooked = Random.nextDouble() < 0.3
            seats.add(if (isBooked) 1 else 0)
        }
    }

    private fun setupGridView() {
        val gridView = findViewById<GridView>(R.id.gridViewSeats)
        seatAdapter = SeatAdapter(this, seats)
        gridView.adapter = seatAdapter
        
        // Use onItemClickListener for better reliability in GridView
        gridView.setOnItemClickListener { _, _, position, _ ->
            toggleSeat(position)
        }
    }

    private fun toggleSeat(position: Int) {
        if (seats[position] == 1) {
            Toast.makeText(this, "This seat is already booked!", Toast.LENGTH_SHORT).show()
            return
        }

        if (seats[position] == 0) {
            seats[position] = 2
            selectedCount++
        } else {
            seats[position] = 0
            selectedCount--
        }

        seatAdapter.notifyDataSetChanged()
        updateSummary()
    }

    private fun updateSummary() {
        val tvSeats = findViewById<TextView>(R.id.tvSummarySeats)
        val tvPrice = findViewById<TextView>(R.id.tvSummaryPrice)
        
        tvSeats.text = "$selectedCount Seats Selected"
        val totalPrice = selectedCount * (event?.price ?: 0.0)
        tvPrice.text = "Total: $${String.format("%.2f", totalPrice)}"
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.btnConfirmBooking).setOnClickListener {
            if (selectedCount > 0) {
                val intent = Intent(this, ConfirmationActivity::class.java)
                intent.putExtra("EVENT_TITLE", event?.title)
                intent.putExtra("SEATS", selectedCount)
                intent.putExtra("PRICE", selectedCount * (event?.price ?: 0.0))
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please select at least one seat.", Toast.LENGTH_SHORT).show()
            }
        }
        
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarBooking).setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (selectedCount > 0) {
            AlertDialog.Builder(this)
                .setTitle("Confirm Exit")
                .setMessage("You have selected seats. Are you sure you want to leave?")
                .setPositiveButton("Yes") { _, _ -> super.onBackPressed() }
                .setNegativeButton("No", null)
                .show()
        } else {
            super.onBackPressed()
        }
    }
}
