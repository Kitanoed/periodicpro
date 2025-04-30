package com.app.periodicpro

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

class ProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etYear: EditText
    private lateinit var etCourse: EditText
    private lateinit var btnEdit: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var profileImage: ImageView

    private var isEditing = false

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                profileImage.setImageURI(uri)
                saveImageUri(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImage = findViewById(R.id.profile_image)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etYear = findViewById(R.id.etYear)
        etCourse = findViewById(R.id.etCourse)
        btnEdit = findViewById(R.id.btnEdit)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, LandingActivity::class.java))
            finish()
        }

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        loadUserData()
        loadImageUri()

        profileImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

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

    private fun saveUserData() {
        with(sharedPreferences.edit()) {
            putString("name", etName.text.toString())
            putString("email", etEmail.text.toString())
            putString("year", etYear.text.toString())
            putString("course", etCourse.text.toString())
            apply()
        }
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

    private fun saveImageUri(uri: Uri) {
        sharedPreferences.edit()
            .putString("profile_image_uri", uri.toString())
            .apply()
    }

    private fun loadImageUri() {
        val uriString = sharedPreferences.getString("profile_image_uri", null)
        if (uriString != null) {
            try {
                val uri = Uri.parse(uriString)
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                profileImage.setImageURI(uri)
            } catch (e: Exception) {
                e.printStackTrace()
                profileImage.setImageResource(R.drawable.ic_profile)
            }
        } else {
            profileImage.setImageResource(R.drawable.ic_profile)
        }
    }
}
