package com.example.gradereportapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var gradeTable: TableLayout
    private lateinit var etSubject: EditText
    private lateinit var etObtained: EditText
    private lateinit var etTotal: EditText
    private lateinit var btnAdd: Button
    private lateinit var tvGPA: TextView
    private lateinit var tvSummary: TextView
    private lateinit var btnPrint: Button

    private var totalSubjects = 0
    private var passedSubjects = 0
    private var failedSubjects = 0
    private var totalGpaPoints = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize views
        gradeTable = findViewById(R.id.gradeTable)
        etSubject = findViewById(R.id.etSubject)
        etObtained = findViewById(R.id.etObtained)
        etTotal = findViewById(R.id.etTotal)
        btnAdd = findViewById(R.id.btnAdd)
        tvGPA = findViewById(R.id.tvGPA)
        tvSummary = findViewById(R.id.tvSummary)
        btnPrint = findViewById(R.id.btnPrint)

        // Initial Data (6 subjects as required)
        val initialSubjects = listOf(
            Triple("Mathematics", 95.0, 100.0),
            Triple("Physics", 82.0, 100.0),
            Triple("Chemistry", 74.0, 100.0),
            Triple("Biology", 65.0, 100.0),
            Triple("English", 55.0, 100.0),
            Triple("History", 35.0, 100.0)
        )

        initialSubjects.forEach { (name, obtained, total) ->
            addSubjectToTable(name, obtained, total)
        }

        btnAdd.setOnClickListener {
            val name = etSubject.text.toString().trim()
            val obtainedStr = etObtained.text.toString().trim()
            val totalStr = etTotal.text.toString().trim()

            if (name.isEmpty() || obtainedStr.isEmpty() || totalStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val obtained = obtainedStr.toDoubleOrNull()
            val total = totalStr.toDoubleOrNull()

            if (obtained == null || total == null || total <= 0 || obtained < 0 || total > 100) {
                Toast.makeText(this, "Invalid marks entered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (obtained > total) {
                Toast.makeText(this, "Obtained marks cannot exceed total marks", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addSubjectToTable(name, obtained, total)

            // Clear inputs
            etSubject.text.clear()
            etObtained.text.clear()
            etTotal.text.clear()
        }

        btnPrint.setOnClickListener {
            Toast.makeText(this, "Generating Report for ${totalSubjects} subjects...", Toast.LENGTH_LONG).show()
        }
    }

    private fun addSubjectToTable(name: String, obtained: Double, total: Double) {
        val percentage = (obtained / total) * 100
        val (grade, points) = calculateGrade(percentage)

        totalSubjects++
        if (grade == "F") {
            failedSubjects++
        } else {
            passedSubjects++
        }
        totalGpaPoints += points

        val tableRow = TableRow(this)
        val rowPadding = (8 * resources.displayMetrics.density).toInt()
        tableRow.setPadding(rowPadding, rowPadding, rowPadding, rowPadding)

        // Shading and Highlighting (Requirement: Fail in red, Pass in green, plus alternate shading)
        val baseColor = if (grade == "F") {
            Color.parseColor("#FFEBEE") // Light Red
        } else {
            Color.parseColor("#E8F5E9") // Light Green
        }

        // Apply subtle alternate shading
        val finalBgColor = if (totalSubjects % 2 == 0) {
            // Slightly darken the base color for even rows
            if (grade == "F") Color.parseColor("#FFCDD2") else Color.parseColor("#C8E6C9")
        } else {
            baseColor
        }

        tableRow.setBackgroundColor(finalBgColor)

        tableRow.addView(createTextView(name, Gravity.START))
        tableRow.addView(createTextView(obtained.toInt().toString(), Gravity.CENTER))
        tableRow.addView(createTextView(total.toInt().toString(), Gravity.CENTER))

        val tvGrade = createTextView(grade, Gravity.CENTER)
        tvGrade.setTypeface(null, Typeface.BOLD)
        if (grade == "F") {
            tvGrade.setTextColor(ContextCompat.getColor(this, R.color.fail_text))
        } else {
            tvGrade.setTextColor(ContextCompat.getColor(this, R.color.pass_text))
        }
        tableRow.addView(tvGrade)

        // Insert before the summary row (which is the last child)
        gradeTable.addView(tableRow, gradeTable.childCount - 1)

        updateStatistics()
    }

    private fun createTextView(text: String, gravity: Int): TextView {
        val tv = TextView(this)
        tv.text = text
        tv.gravity = gravity
        tv.setPadding(8, 8, 8, 8)
        tv.setTextColor(Color.BLACK)
        return tv
    }

    private fun calculateGrade(percentage: Double): Pair<String, Double> {
        return when {
            percentage >= 90 -> "A+" to 4.0
            percentage >= 80 -> "A" to 3.7
            percentage >= 70 -> "B+" to 3.3
            percentage >= 60 -> "B" to 3.0
            percentage >= 50 -> "C" to 2.0
            percentage >= 40 -> "D" to 1.0
            else -> "F" to 0.0
        }
    }

    private fun updateStatistics() {
        val gpa = if (totalSubjects > 0) totalGpaPoints / totalSubjects else 0.0
        tvGPA.text = "GPA: %.2f".format(gpa)
        tvSummary.text = "Total: $totalSubjects | Passed: $passedSubjects | Failed: $failedSubjects"
    }
}