package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameField = findViewById<TextInputEditText>(R.id.edittext_usernameRegister)
        val passwordField = findViewById<TextInputEditText>(R.id.edittext_passwordRegister)
        val confirmPasswordField = findViewById<TextInputEditText>(R.id.edittext_confirmPassword)
        val registerButton = findViewById<MaterialButton>(R.id.button_goToLogin)
        val loginLink = findViewById<TextView>(R.id.button_goBackToLogin)

        // Register Button Click
        registerButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("UserCredentials", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("savedUsername", username)
                putString("savedPassword", password)
                apply()
            }

            Toast.makeText(this, "Registration successful! Please log in.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        // Login Link Click
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
