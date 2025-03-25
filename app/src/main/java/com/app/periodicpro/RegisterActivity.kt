package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.app.periodicpro.utils.toast

class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edittext_usernameRegister = findViewById<EditText>(R.id.edittext_usernameRegister)
        val edittext_passwordRegister = findViewById<EditText>(R.id.edittext_passwordRegister)
        val edittext_confirmPassword = findViewById<EditText>(R.id.edittext_confirmPassword)

        val button_goToLogin = findViewById<Button>(R.id.button_goToLogin)
        button_goToLogin.setOnClickListener {

            val username = edittext_usernameRegister.text
            val password = edittext_passwordRegister.text
            val confirmPassword = edittext_confirmPassword.text

            if(username.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()){
                this.toast("Username, Password, and Confirm Password cannot be empty!")
                return@setOnClickListener
            }

            startActivity(
                Intent(this, LoginActivity::class.java).apply {
                    putExtra("username", username.toString())
                    putExtra("password", password.toString())
                }
            )
        }
    }
}