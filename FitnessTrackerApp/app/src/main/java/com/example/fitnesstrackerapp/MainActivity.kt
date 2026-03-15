package com.example.fitnesstrackerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvStepsCount: TextView
    private lateinit var tvCaloriesCount: TextView
    private lateinit var tvWaterCount: TextView
    private lateinit var tvWorkoutCount: TextView
    private lateinit var tvProgressPercent: TextView
    private lateinit var pbGoal: ProgressBar
    private lateinit var tvCurrentDate: TextView
    private lateinit var btnUpdateStats: Button

    private var currentSteps = 0
    private var dailyGoal = 10000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Views
        tvStepsCount = findViewById(R.id.tvStepsCount)
        tvCaloriesCount = findViewById(R.id.tvCaloriesCount)
        tvWaterCount = findViewById(R.id.tvWaterCount)
        tvWorkoutCount = findViewById(R.id.tvWorkoutCount)
        tvProgressPercent = findViewById(R.id.tvProgressPercent)
        pbGoal = findViewById(R.id.pbGoal)
        tvCurrentDate = findViewById(R.id.tvCurrentDate)
        btnUpdateStats = findViewById(R.id.btnUpdateStats)

        // Set Current Date
        setCurrentDate()

        // Button Click Listener
        btnUpdateStats.setOnClickListener {
            showUpdateDialog()
        }
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
        tvCurrentDate.text = dateFormat.format(calendar)
    }

    private fun showUpdateDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update_stats, null)
        val etSteps = dialogView.findViewById<EditText>(R.id.etStepsInput)
        val etCalories = dialogView.findViewById<EditText>(R.id.etCaloriesInput)
        val etWater = dialogView.findViewById<EditText>(R.id.etWaterInput)
        val etWorkout = dialogView.findViewById<EditText>(R.id.etWorkoutInput)

        // Pre-fill with current values
        etSteps.setText(currentSteps.toString())

        AlertDialog.Builder(this)
            .setTitle("Update Daily Stats")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val newSteps = etSteps.text.toString().toIntOrNull() ?: currentSteps
                val newCalories = etCalories.text.toString().toIntOrNull() ?: 0
                val newWater = etWater.text.toString().toDoubleOrNull() ?: 0.0
                val newWorkout = etWorkout.text.toString().toIntOrNull() ?: 0

                updateStats(newSteps, newCalories, newWater, newWorkout)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun updateStats(steps: Int, calories: Int, water: Double, workout: Int) {
        currentSteps = steps
        tvStepsCount.text = steps.toString()
        tvCaloriesCount.text = calories.toString()
        tvWaterCount.text = String.format("%.1f", water)
        tvWorkoutCount.text = workout.toString()

        // Calculate progress
        val progress = if (dailyGoal > 0) {
            (steps.toFloat() / dailyGoal * 100).toInt()
        } else {
            0
        }

        val displayProgress = if (progress > 100) 100 else progress
        pbGoal.progress = displayProgress
        tvProgressPercent.text = "$progress%"

        if (progress >= 100) {
            Toast.makeText(this, "Amazing! You've reached your daily goal! 🔥", Toast.LENGTH_LONG).show()
        }
    }

}