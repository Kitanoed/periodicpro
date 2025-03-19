package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edittext_username = findViewById<EditText>(R.id.edittext_username)
        val edittext_password = findViewById<EditText>(R.id.edittext_password)
        val button_login = findViewById<TextView>(R.id.button_login)
        val button_register = findViewById<TextView>(R.id.button_register)

        intent?.let {
            it.getStringExtra("username")?.let {username ->
                edittext_username.setText(username)
            }

            it.getStringExtra("password")?.let {password ->
                edittext_password.setText(password)
            }
        }

        button_login.setOnClickListener {
            val username = edittext_username.text.toString()
            val password = edittext_password.text.toString()

            if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "Username and Password cannot be empty!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (username.contains("@") && password == "456") {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_LONG).show()
            }
        }

        button_register.setOnClickListener {
            Log.e("PeriodicPro Project", "Register button clicked!")
            Toast.makeText(this, "Opening register screen!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
