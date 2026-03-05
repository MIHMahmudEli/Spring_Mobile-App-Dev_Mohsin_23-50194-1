package com.example.studentregistrationapp

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studentregistrationapp.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //access all elements
        val studentID = findViewById<EditText>(R.id.studentIdEditText)
        val fullName = findViewById<EditText>(R.id.fullNameEditText)
        val email = findViewById<EditText>(R.id.emailEditText)
        val pass = findViewById<EditText>(R.id.passwordEditText)
        val age = findViewById<EditText>(R.id.ageEditText)
        val gender = findViewById<RadioGroup>(R.id.genderRadioGroup)
        val football = findViewById<CheckBox>(R.id.footballCheckBox)
        val cricket = findViewById<CheckBox>(R.id.cricketCheckBox)
        val basketball = findViewById<CheckBox>(R.id.basketballCheckBox)
        val badminton = findViewById<CheckBox>(R.id.badmintonCheckBox)
        val country = findViewById<Spinner>(R.id.countrySpinner)
        val datePicker = findViewById<Button>(R.id.datePickerButton)
        val selectedDate = findViewById<TextView>(R.id.selectedDateTextView)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resetButton = findViewById<Button>(R.id.resetButton)

        // Set Country Spinner from arrays.xml
        ArrayAdapter.createFromResource(
            this,
            R.array.countries_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            country.adapter = adapter
        }

        //Date selection
        datePicker.setOnClickListener {
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    selectedDate.text = "$d/${m + 1}/$y"
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //submit validation
        submitButton.setOnClickListener {
            //collect data
            val studentIdText = studentID.text.toString().trim()
            val fullNameText = fullName.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passText = pass.text.toString().trim()
            val ageText = age.text.toString().trim()
            val selectedGenderId = gender.checkedRadioButtonId
            val countryName = country.selectedItem.toString()
            val dob = selectedDate.text.toString()

            //collect selected sports
            val selectedSports = mutableListOf<String>()
            if (football.isChecked) {
                selectedSports.add("Football")
            }
            if (cricket.isChecked) {
                selectedSports.add("Cricket")
            }
            if (basketball.isChecked) {
                selectedSports.add("Basketball")
            }
            if (badminton.isChecked) {
                selectedSports.add("Badminton")
            }

            // Validation Logic
            var isValid = true

            // No field should be empty
            if (studentIdText.isEmpty() || fullNameText.isEmpty() || emailText.isEmpty() ||
                passText.isEmpty() || ageText.isEmpty() || dob == "No Date Selected") {
                isValid = false
            }

            // Email must contain "@"
            if (!emailText.contains("@")) {
                isValid = false
            }

            // Age must be greater than 0
            val ageNum = ageText.toIntOrNull() ?: 0
            if (ageNum <= 0) {
                isValid = false
            }

            // One gender must be selected
            if (selectedGenderId == -1) {
                isValid = false
            }

            if(isValid) {

                val genderButton = findViewById<RadioButton>(selectedGenderId)
                val genderName = genderButton.text.toString()
                val sports = if (selectedSports.isEmpty()) "None" else selectedSports.joinToString(", ")


                //display data in AlertDialog for full visibility
                val result = "Student ID: $studentIdText\n" +
                        "Full Name: $fullNameText\n" +
                        "Email: $emailText\n" +
                        "Password: $passText\n" +
                        "Age: $ageText\n" +
                        "Gender: $genderName\n" +
                        "Sports: $sports\n" +
                        "Country: $countryName\n" +
                        "Date of Birth: $dob"

                AlertDialog.Builder(this)
                    .setTitle("Registration Summary")
                    .setMessage(result)
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()

            }
            else {
                Toast.makeText(this, "Please fill all the fields correctly", Toast.LENGTH_LONG).show()
            }
        }
        resetButton.setOnClickListener {
            studentID.text.clear()
            fullName.text.clear()
            email.text.clear()
            pass.text.clear()
            age.text.clear()
            gender.clearCheck()
            football.isChecked = false
            cricket.isChecked = false
            basketball.isChecked = false
            badminton.isChecked = false
            country.setSelection(0)
            selectedDate.text = "No Date Selected"
            Toast.makeText(this, "All fields are reset", Toast.LENGTH_LONG).show()

        }
    }
}
