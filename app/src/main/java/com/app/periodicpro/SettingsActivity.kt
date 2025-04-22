package com.app.periodicpro

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val button_developer = findViewById<Button>(R.id.button_developer)
        button_developer.setOnClickListener {
            Log.e("PeriodicPro Project", "Developer Button clicked!")

            val intent = Intent(this, DeveloperActivity::class.java)
            startActivity(intent)
        }

        val button_help = findViewById<Button>(R.id.button_help)
        button_help.setOnClickListener {
            Log.e("PeriodicPro Project", "Help Button clicked!")

            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }

        val button_privacy = findViewById<Button>(R.id.button_privacy)
        button_privacy.setOnClickListener {
            Log.e("PeriodicPro Project", "Privacy Button clicked!")

            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        val button_logout = findViewById<Button>(R.id.button_logout)
        button_logout.setOnClickListener {
            Log.e("PeriodicPro Project", "Logout Button clicked!")

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
}
