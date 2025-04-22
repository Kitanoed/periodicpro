package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.app.Fragment
import com.app.periodicpro.fragments.NavigationFragment

class LandingActivity : Activity(), NavigationFragment.OnNavigationItemSelectedListener {
    private lateinit var navigationFragment: NavigationFragment
    private var backPressedTime = 0L
    private lateinit var backToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        try {
            // Initialize navigation fragment
            navigationFragment = NavigationFragment()

            // Set up navigation fragment
            fragmentManager.beginTransaction()
                .replace(R.id.navigation_container, navigationFragment)
                .commit()

            navigationFragment.setOnNavigationItemSelectedListener(this)

            // Set up button click listeners
            findViewById<Button>(R.id.button_pTable).setOnClickListener {
                val intent = Intent(this, PeriodicTableActivity::class.java)
                startActivity(intent)
            }

            findViewById<Button>(R.id.button_favorites).setOnClickListener {
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
            }

            // Set up search functionality
            val editTextSearch = findViewById<EditText>(R.id.edittext_search)
            editTextSearch.setOnEditorActionListener { textView, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                ) {
                    performSearch(textView.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            // Add search icon click listener
            findViewById<ImageView>(R.id.search_icon).setOnClickListener {
                performSearch(editTextSearch.text.toString())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error initializing the app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            val intent = Intent(this, SearchResultsActivity::class.java)
            intent.putExtra("SEARCH_QUERY", query)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavigationItemSelected(itemId: Int) {
        try {
            when (itemId) {
                R.id.button_landing -> {
                    // Already on home screen
                    Toast.makeText(this, "You're already in the home screen!", Toast.LENGTH_SHORT).show()
                }
                R.id.button_table -> {
                    val intent = Intent(this, PeriodicTableActivity::class.java)
                    startActivity(intent)
                }
                R.id.button_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.button_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error navigating: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            backToast = Toast.makeText(this, "Press again to log out", Toast.LENGTH_SHORT)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
