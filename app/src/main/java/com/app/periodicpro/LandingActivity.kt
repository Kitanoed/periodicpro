package com.app.periodicpro

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.app.periodicpro.data.Element
import com.app.periodicpro.data.ElementCategory
import com.app.periodicpro.fragments.NavigationFragment
import org.json.JSONArray
import org.json.JSONObject

class LandingActivity : Activity(), NavigationFragment.OnNavigationItemSelectedListener {
    private lateinit var navigationFragment: NavigationFragment
    private var backPressedTime = 0L
    private lateinit var backToast: Toast
    private lateinit var listRecent: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        navigationFragment = NavigationFragment()
        fragmentManager.beginTransaction()
            .replace(R.id.navigation_container, navigationFragment)
            .commit()
        navigationFragment.setOnNavigationItemSelectedListener(this)

        val editTextSearch = findViewById<EditText>(R.id.edittext_search)
        val searchIcon = findViewById<ImageView>(R.id.search_icon)
        val buttonTable = findViewById<Button>(R.id.button_pTable)
        val buttonFavorites = findViewById<Button>(R.id.button_favorites)
        listRecent = findViewById(R.id.recent_list)

        buttonTable.setOnClickListener {
            startActivity(Intent(this, PeriodicTableActivity::class.java))
        }

        buttonFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        editTextSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                performSearch(textView.text.toString())
                true
            } else false
        }

        searchIcon.setOnClickListener {
            performSearch(editTextSearch.text.toString())
        }

        loadRecentElements()
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

    @SuppressLint("MissingInflatedId")
    private fun loadRecentElements() {
        listRecent.removeAllViews()
        val prefs = getSharedPreferences("RecentElements", MODE_PRIVATE)
        val json = prefs.getString("recent", "[]")
        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val symbol = obj.getString("symbol")
            val number = obj.getInt("number")

            val view = layoutInflater.inflate(R.layout.recent_item, listRecent, false)
            view.findViewById<TextView>(R.id.recent_name).text = name
            view.findViewById<TextView>(R.id.recent_number).text = "Atomic number: $number"
            view.findViewById<TextView>(R.id.recent_symbol).text = symbol

            listRecent.addView(view)
        }
    }

    override fun onNavigationItemSelected(itemId: Int) {
        val targetActivity = when (itemId) {
            R.id.button_landing -> null
            R.id.button_table -> PeriodicTableActivity::class.java
            R.id.button_profile -> ProfileActivity::class.java
            R.id.button_settings -> SettingsActivity::class.java
            else -> null
        }

        if (targetActivity != null) {
            startActivity(Intent(this, targetActivity))
        } else {
            Toast.makeText(this, "You're already in the home screen!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        } else {
            backToast = Toast.makeText(this, "Press again to log out", Toast.LENGTH_SHORT)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
