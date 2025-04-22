package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edittext_usernameRegister = findViewById<EditText>(R.id.edittext_usernameRegister)
        val edittext_passwordRegister = findViewById<EditText>(R.id.edittext_passwordRegister)
        val edittext_confirmPassword = findViewById<EditText>(R.id.edittext_confirmPassword)

        val button_goToLogin = findViewById<Button>(R.id.button_goToLogin)
        button_goToLogin.setOnClickListener {

            val username = edittext_usernameRegister.text.toString()
            val password = edittext_passwordRegister.text.toString()
            val confirmPassword = edittext_confirmPassword.text.toString()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Username, Password, and Confirm Password cannot be empty!", Toast.LENGTH_LONG).show()
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

            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show()

            // Go back to login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
