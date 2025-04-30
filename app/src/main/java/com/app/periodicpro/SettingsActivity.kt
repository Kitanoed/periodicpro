package com.app.periodicpro

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val switchNotifications = findViewById<Switch>(R.id.switch_notifications)
        val btnClearRecent = findViewById<Button>(R.id.button_clear_recent)
        val btnDeveloper = findViewById<Button>(R.id.button_developer)
        val btnHelp = findViewById<Button>(R.id.button_help)
        val btnPrivacy = findViewById<Button>(R.id.button_privacy)
        val btnLogout = findViewById<Button>(R.id.button_logout)

        btnBack.setOnClickListener {
            finish()
        }

        // Show toast for notifications toggle
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(
                this,
                if (isChecked) "Notifications Enabled" else "Notifications Disabled",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Clear recent elements from SharedPreferences
        btnClearRecent.setOnClickListener {
            val prefs = getSharedPreferences("RecentElements", MODE_PRIVATE)
            prefs.edit().clear().apply()
            Toast.makeText(this, "Recent elements cleared", Toast.LENGTH_SHORT).show()
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

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to log out of your account?")
            .setPositiveButton("Logout") { _, _ ->
                Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show()
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
