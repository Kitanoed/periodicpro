package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class HelpActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val btnBack = findViewById<ImageButton>(R.id.btnBackHelp)
        btnBack.setOnClickListener {
            finish()
        }
        
        // Set up contact button
        val btnContact = findViewById<MaterialButton>(R.id.btn_contact_support)
        btnContact?.setOnClickListener {
            // Open email with pre-filled subject
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@periodicpro.com")
                putExtra(Intent.EXTRA_SUBJECT, "PeriodicPro App Support Request")
            }
            
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "No email app found. Please email support@periodicpro.com",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        
        // Set up FAQ section expandable items
        setupFaqItems()
    }
    
    private fun setupFaqItems() {
        val faqItems = arrayOf(
            Pair(R.id.faq_item_1_header, R.id.faq_item_1_content),
            Pair(R.id.faq_item_2_header, R.id.faq_item_2_content),
            Pair(R.id.faq_item_3_header, R.id.faq_item_3_content)
        )
        
        faqItems.forEach { (headerId, contentId) ->
            val header = findViewById<TextView>(headerId)
            val content = findViewById<TextView>(contentId)
            
            // Initially hide content
            content?.visibility = TextView.GONE
            
            // Set up click listener to expand/collapse
            header?.setOnClickListener {
                if (content?.visibility == TextView.VISIBLE) {
                    content.visibility = TextView.GONE
                } else {
                    content?.visibility = TextView.VISIBLE
                }
            }
        }
    }
}
