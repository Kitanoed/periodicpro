package com.app.periodicpro

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.snackbar.Snackbar

class PrivacyPolicyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        val btnBack = findViewById<ImageButton>(R.id.btnBackPrivacy)
        btnBack.setOnClickListener {
            finish() // Return to Settings
        }
        
        // Set up links or additional functionality if needed
        val helpLink = findViewById<android.widget.TextView>(R.id.tvContactUs)
        helpLink?.setOnClickListener {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Contact form will be available in the next update",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}
