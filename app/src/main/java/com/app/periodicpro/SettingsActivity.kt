package com.app.periodicpro

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : Activity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val switchNotifications = findViewById<SwitchMaterial>(R.id.switch_notifications)
        val btnClearRecent = findViewById<MaterialButton>(R.id.button_clear_recent)
        val btnDeveloper = findViewById<MaterialButton>(R.id.button_developer)
        val btnHelp = findViewById<MaterialButton>(R.id.button_help)
        val btnPrivacy = findViewById<MaterialButton>(R.id.button_privacy)
        val btnLogout = findViewById<MaterialButton>(R.id.button_logout)

        // Load saved preferences
        switchNotifications.isChecked = sharedPreferences.getBoolean("notifications_enabled", true)

        btnBack.setOnClickListener {
            finish()
        }

        // Show Snackbar for notifications toggle and save the setting
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            saveNotificationSetting(isChecked)
            
            Snackbar.make(
                findViewById(android.R.id.content),
                if (isChecked) "Notifications Enabled" else "Notifications Disabled",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        // Clear recent elements from SharedPreferences
        btnClearRecent.setOnClickListener {
            val prefs = getSharedPreferences("RecentElements", MODE_PRIVATE)
            prefs.edit().clear().apply()
            
            Snackbar.make(
                findViewById(android.R.id.content),
                "Recent elements cleared",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        // Navigate to other sections
        btnDeveloper.setOnClickListener {
            startActivity(Intent(this, DeveloperActivity::class.java))
        }

        btnHelp.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }

        btnPrivacy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }

        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }
    
    private fun saveNotificationSetting(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("notifications_enabled", enabled).apply()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to log out of your account?")
            .setPositiveButton("Logout") { _, _ ->
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Logged out successfully!",
                    Snackbar.LENGTH_SHORT
                ).show()
                
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onBackPressed() {
        finish()
    }
}
