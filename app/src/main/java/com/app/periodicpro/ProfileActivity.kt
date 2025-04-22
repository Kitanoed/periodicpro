package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ProfileActivity : Activity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etYear: EditText
    lateinit var etCourse: EditText
    lateinit var btnEdit: Button
    lateinit var sharedPreferences: SharedPreferences

    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etYear = findViewById(R.id.etYear)
        etCourse = findViewById(R.id.etCourse)
        btnEdit = findViewById(R.id.btnEdit)


        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
            finish()
        }

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)

        loadUserData()

        btnEdit.setOnClickListener {
            if (isEditing) {
                saveUserData()
                toggleEditing(false)

                Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()
            } else {
                toggleEditing(true)
            }
        }
    }

    private fun toggleEditing(enable: Boolean) {
        etName.isEnabled = enable
        etEmail.isEnabled = enable
        etYear.isEnabled = enable
        etCourse.isEnabled = enable

        val textColor = Color.WHITE
        etName.setTextColor(textColor)
        etEmail.setTextColor(textColor)
        etYear.setTextColor(textColor)
        etCourse.setTextColor(textColor)

        btnEdit.text = if (enable) "Done" else "Edit"
        isEditing = enable
    }

    private fun loadUserData() {
        etName.setText(sharedPreferences.getString("name", "User112233"))
        etEmail.setText(sharedPreferences.getString("email", "User@gmail.com"))
        etYear.setText(sharedPreferences.getString("year", "Second Year"))
        etCourse.setText(sharedPreferences.getString("course", "Computer Science"))

        val textColor = Color.WHITE
        etName.setTextColor(textColor)
        etEmail.setTextColor(textColor)
        etYear.setTextColor(textColor)
        etCourse.setTextColor(textColor)
    }

    private fun saveUserData() {
        with(sharedPreferences.edit()) {
            putString("name", etName.text.toString())
            putString("email", etEmail.text.toString())
            putString("year", etYear.text.toString())
            putString("course", etCourse.text.toString())
            apply()
        }
    }
}
