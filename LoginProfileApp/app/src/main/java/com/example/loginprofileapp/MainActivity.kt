package com.example.loginprofileapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private lateinit var loginContainer: RelativeLayout
    private lateinit var profileCard: MaterialCardView
    private lateinit var loadingPb: ProgressBar
    private lateinit var usernameEt: EditText
    private lateinit var passwordEt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginContainer = findViewById(R.id.loginContainer)
        profileCard = findViewById(R.id.profileCard)
        loadingPb = findViewById(R.id.loadingPb)
        usernameEt = findViewById(R.id.usernameEt)
        passwordEt = findViewById(R.id.passwordEt)

        var loginBtn = findViewById<Button>(R.id.loginBtn)
        var logoutBtn = findViewById<Button>(R.id.logoutBtn)
        var forgotPasswordTv = findViewById<TextView>(R.id.forgotPasswordTv)

        loginBtn.setOnClickListener {

            var username = usernameEt.text.toString()
            var password = passwordEt.text.toString()

            if(username == "admin" && password == "1234"){

                showLoadingAndTransition()

            } else{
                Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
            }
        }

        logoutBtn.setOnClickListener {
            loginContainer.visibility = View.VISIBLE
            profileCard.visibility = View.GONE
            usernameEt.text.clear()
            passwordEt.text.clear()
        }

        forgotPasswordTv.setOnClickListener {
            Toast.makeText(this, "Forgot password link sent to your email!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoadingAndTransition(){
        loginContainer.visibility = View.GONE
        profileCard.visibility = View.VISIBLE

        // Mimic network delay
        Handler(Looper.getMainLooper()).postDelayed({
            loadingPb.visibility = View.GONE
            profileCard.visibility = View.VISIBLE
        }, 1500) // 1.5 seconds delay
    }
}