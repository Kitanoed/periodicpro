package com.app.periodicpro

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.graphics.Rect

class ProfileActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etYear: TextInputEditText
    private lateinit var etCourse: TextInputEditText
    private lateinit var btnEdit: MaterialButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var profileImage: ImageView

    private var isEditing = false

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    profileImage.setImageURI(uri)
                    saveImageUri(uri)
                    
                    // Show confirmation message
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Profile picture updated successfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Show error message
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Could not update profile picture",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
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

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        loadUserData()
        loadImageUri()

        profileImage.setOnClickListener {
            if (isEditing) {
                imagePickerLauncher.launch("image/*")
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Enable edit mode to change your profile picture",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        btnEdit.setOnClickListener {
            if (isEditing) {
                saveUserData()
                toggleEditing(false)
                
                // Hide keyboard when saving
                hideKeyboard()
                
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Profile updated successfully!",
                    Snackbar.LENGTH_SHORT
                ).show()
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

        btnEdit.text = if (enable) "Save Changes" else "Edit Profile"
        btnEdit.backgroundTintList = if (enable) {
            ContextCompat.getColorStateList(this, android.R.color.holo_green_dark)
        } else {
            ContextCompat.getColorStateList(this, R.color.purple_500)
        }
        
        isEditing = enable
        
        // When enabling edit mode, focus on the first field
        if (enable) {
            etName.requestFocus()
            
            // Set focus change listeners to scroll to the focused view
            setFocusChangeListeners()
        }
    }
    
    private fun setFocusChangeListeners() {
        val scrollView = findViewById<ScrollView>(R.id.profile_scroll_view)
        
        // Make ScrollView remain scrollable even when fields have focus
        scrollView.isNestedScrollingEnabled = true
        
        val focusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // Let the system adjust first, then make minor adjustments if needed
                view.postDelayed({
                    // Make sure the view is visible
                    view.requestRectangleOnScreen(Rect(0, 0, view.width, view.height + 150), true)
                    
                    // After adjustment, re-enable smooth scrolling
                    view.postDelayed({
                        scrollView.isSmoothScrollingEnabled = true
                    }, 200)
                }, 300)
            }
        }
        
        // Set up touch listeners to ensure scrolling works while editing
        val touchFields = arrayOf(etName, etEmail, etYear, etCourse)
        touchFields.forEach { field ->
            // Allow parent scrolling when reaching the edge of the input field
            field.setOnTouchListener { v, event ->
                scrollView.requestDisallowInterceptTouchEvent(false)
                false // Don't consume the event, allow normal handling
            }
        }
        
        etName.onFocusChangeListener = focusChangeListener
        etEmail.onFocusChangeListener = focusChangeListener
        etYear.onFocusChangeListener = focusChangeListener
        etCourse.onFocusChangeListener = focusChangeListener
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

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
