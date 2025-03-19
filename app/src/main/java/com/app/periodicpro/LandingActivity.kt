package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.app.periodicpro.utils.toast


class LandingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val button_pTable = findViewById<Button>(R.id.button_pTable)
        button_pTable.setOnClickListener {
            Log.e("PeriodicPro Project", "Button is clicked!")
            this.toast("No Periodic Table screen yet!")
        }
        val button_table = findViewById<Button>(R.id.button_table)
        button_table.setOnClickListener {
            Log.e("PeriodicPro Project", "Button is clicked!")
            this.toast("No Periodic Table screen yet!")

        }

        val button_favorites = findViewById<Button>(R.id.button_favorites)
        button_favorites.setOnClickListener {
            Log.e("PeriodicPro Project", "Button is clicked!")
            this.toast("Opening Favorites screen!")
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

        val button_landing = findViewById<Button>(R.id.button_landing)
        button_landing.setOnClickListener {
            Log.e("PeriodicPro Project", "Button is clicked!")
            this.toast("You're already in the home screen!")
        }

        val button_profile = findViewById<Button>(R.id.button_profile)
        button_profile.setOnClickListener {
            Log.e("PeriodicPro Project", "Button is clicked!")
            this.toast("Opening profile screen!")

            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val button_settings = findViewById<Button>(R.id.button_settings)
        button_settings.setOnClickListener {
            Log.e("PeriodicPro Project", "Button is clicked!")
            this.toast("Opening settings screen!")

            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}