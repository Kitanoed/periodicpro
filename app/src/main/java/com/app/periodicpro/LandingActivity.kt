package com.app.periodicpro

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.app.periodicpro.fragments.NavigationFragment
import org.json.JSONArray
import org.json.JSONObject

class LandingActivity : Activity(), NavigationFragment.OnNavigationItemSelectedListener {
    private lateinit var navigationFragment: NavigationFragment
    private var backPressedTime = 0L
    private lateinit var backToast: Toast
    private lateinit var listRecent: LinearLayout
    private lateinit var searchBox: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        navigationFragment = NavigationFragment()
        fragmentManager.beginTransaction()
            .replace(R.id.navigation_container, navigationFragment)
            .commit()
        navigationFragment.setOnNavigationItemSelectedListener(this)

        // Views
        searchBox = findViewById(R.id.edittext_search)
        val searchIcon = findViewById<ImageView>(R.id.search_icon)
        val buttonTable = findViewById<Button>(R.id.button_pTable)
        val buttonFavorites = findViewById<Button>(R.id.button_favorites)
        listRecent = findViewById(R.id.recent_list)

        // Dropdown suggestions
        val elementNames = getAllElementNames()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, elementNames)
        searchBox.setAdapter(adapter)

        // Search by Enter key
        searchBox.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                performSearch(view.text.toString())
                true
            } else false
        }

        // Search by icon click
        searchIcon.setOnClickListener {
            performSearch(searchBox.text.toString())
        }

        buttonTable.setOnClickListener {
            startActivity(Intent(this, PeriodicTableActivity::class.java))
        }

        buttonFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        // Load recent elements initially
        loadRecentElements()
    }

    // 🔁 Reload recent elements every time user returns to this activity
    override fun onResume() {
        super.onResume()
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


        val limit = 5
        val start = maxOf(0, jsonArray.length() - limit)

        for (i in start until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val symbol = obj.getString("symbol")
            val number = obj.getInt("number")

            val view = layoutInflater.inflate(R.layout.recent_item, listRecent, false)
            view.findViewById<TextView>(R.id.recent_name).text = name
            view.findViewById<TextView>(R.id.recent_number).text = "Atomic number: $number"
            view.findViewById<TextView>(R.id.recent_symbol).text = symbol

            view.setOnClickListener {
                showElementPopup(name, symbol, number)
            }

            listRecent.addView(view)
        }
    }


    private fun showElementPopup(name: String, symbol: String, number: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.element_popup)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        dialog.findViewById<TextView>(R.id.popup_name).text = "$name ($symbol)"
        dialog.findViewById<TextView>(R.id.popup_atomic_number).text = "Atomic Number: $number"
        dialog.findViewById<TextView>(R.id.popup_row).text = "-"
        dialog.findViewById<TextView>(R.id.popup_column).text = "-"
        dialog.findViewById<TextView>(R.id.popup_category).text = "-"
        dialog.findViewById<Button>(R.id.popup_close_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getAllElementNames(): List<String> {
        return listOf(
            "Hydrogen", "Helium", "Lithium", "Beryllium", "Boron", "Carbon", "Nitrogen", "Oxygen",
            "Fluorine", "Neon", "Sodium", "Magnesium", "Aluminum", "Silicon", "Phosphorus",
            "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium", "Scandium", "Titanium", "Vanadium",
            "Chromium", "Manganese", "Iron", "Cobalt", "Nickel", "Copper", "Zinc", "Gallium", "Germanium",
            "Arsenic", "Selenium", "Bromine", "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium",
            "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium", "Palladium", "Silver",
            "Cadmium", "Indium", "Tin", "Antimony", "Tellurium", "Iodine", "Xenon", "Cesium", "Barium",
            "Lanthanum", "Cerium", "Praseodymium", "Neodymium", "Promethium", "Samarium", "Europium",
            "Gadolinium", "Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium",
            "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium", "Osmium", "Iridium", "Platinum",
            "Gold", "Mercury", "Thallium", "Lead", "Bismuth", "Polonium", "Astatine", "Radon",
            "Francium", "Radium", "Actinium", "Thorium", "Protactinium", "Uranium", "Neptunium",
            "Plutonium", "Americium", "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium",
            "Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium", "Seaborgium",
            "Bohrium", "Hassium", "Meitnerium", "Darmstadtium", "Roentgenium", "Copernicium",
            "Nihonium", "Flerovium", "Moscovium", "Livermorium", "Tennessine", "Oganesson"
        )
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
