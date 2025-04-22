package com.app.periodicpro

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView

class PrivacyPolicyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        val btnBack = findViewById<ImageView>(R.id.btnBackPrivacy)
        btnBack.setOnClickListener {
            finish() // Return to Settings
        }
    }
}
