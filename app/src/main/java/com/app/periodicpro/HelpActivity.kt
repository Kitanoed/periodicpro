package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView

class HelpActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)


        val btnBack = findViewById<ImageView>(R.id.btnBackHelp)
        btnBack.setOnClickListener {
            finish()
        }
    }
}
