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
import com.app.periodicpro.data.Element
import com.app.periodicpro.data.ElementCategory

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
        
        // Find the element in the list to get its category
        val elementData = getAllElements().find { it.name == name }
        if (elementData != null) {
            dialog.findViewById<TextView>(R.id.popup_row).text = "Row: ${elementData.row}"
            dialog.findViewById<TextView>(R.id.popup_column).text = "Column: ${elementData.column}"
            dialog.findViewById<TextView>(R.id.popup_category).text = 
                "Category: ${elementData.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}"
        } else {
            dialog.findViewById<TextView>(R.id.popup_row).text = "-"
            dialog.findViewById<TextView>(R.id.popup_column).text = "-"
            dialog.findViewById<TextView>(R.id.popup_category).text = "-"
        }

        dialog.findViewById<Button>(R.id.popup_close_button).setOnClickListener {
            dialog.dismiss()
        }

        // Save to recent elements
        val prefs = getSharedPreferences("RecentElements", MODE_PRIVATE)
        val listJson = JSONArray(prefs.getString("recent", "[]"))
        val newObj = JSONObject().apply {
            put("name", name)
            put("symbol", symbol)
            put("number", number)
        }

        val updated = JSONArray()
        updated.put(newObj)
        for (i in 0 until listJson.length()) {
            val item = listJson.getJSONObject(i)
            if (item.getString("name") != name) {
                updated.put(item)
            }
        }

        while (updated.length() > 5) {
            updated.remove(updated.length() - 1)
        }

        prefs.edit().putString("recent", updated.toString()).apply()

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

    private fun getAllElements(): List<Element> {
        return listOf(
            // Period 1
            Element(1, "H", "Hydrogen", 1, 1, ElementCategory.NONMETAL),
            Element(2, "He", "Helium", 1, 18, ElementCategory.NOBLE_GAS),

            // Period 2
            Element(3, "Li", "Lithium", 2, 1, ElementCategory.ALKALI_METAL),
            Element(4, "Be", "Beryllium", 2, 2, ElementCategory.ALKALINE_EARTH_METAL),
            Element(5, "B", "Boron", 2, 13, ElementCategory.METALLOID),
            Element(6, "C", "Carbon", 2, 14, ElementCategory.NONMETAL),
            Element(7, "N", "Nitrogen", 2, 15, ElementCategory.NONMETAL),
            Element(8, "O", "Oxygen", 2, 16, ElementCategory.NONMETAL),
            Element(9, "F", "Fluorine", 2, 17, ElementCategory.HALOGEN),
            Element(10, "Ne", "Neon", 2, 18, ElementCategory.NOBLE_GAS),

            // Period 3
            Element(11, "Na", "Sodium", 3, 1, ElementCategory.ALKALI_METAL),
            Element(12, "Mg", "Magnesium", 3, 2, ElementCategory.ALKALINE_EARTH_METAL),
            Element(13, "Al", "Aluminum", 3, 13, ElementCategory.POST_TRANSITION_METAL),
            Element(14, "Si", "Silicon", 3, 14, ElementCategory.METALLOID),
            Element(15, "P", "Phosphorus", 3, 15, ElementCategory.NONMETAL),
            Element(16, "S", "Sulfur", 3, 16, ElementCategory.NONMETAL),
            Element(17, "Cl", "Chlorine", 3, 17, ElementCategory.HALOGEN),
            Element(18, "Ar", "Argon", 3, 18, ElementCategory.NOBLE_GAS),

            // Period 4
            Element(19, "K", "Potassium", 4, 1, ElementCategory.ALKALI_METAL),
            Element(20, "Ca", "Calcium", 4, 2, ElementCategory.ALKALINE_EARTH_METAL),
            Element(21, "Sc", "Scandium", 4, 3, ElementCategory.TRANSITION_METAL),
            Element(22, "Ti", "Titanium", 4, 4, ElementCategory.TRANSITION_METAL),
            Element(23, "V", "Vanadium", 4, 5, ElementCategory.TRANSITION_METAL),
            Element(24, "Cr", "Chromium", 4, 6, ElementCategory.TRANSITION_METAL),
            Element(25, "Mn", "Manganese", 4, 7, ElementCategory.TRANSITION_METAL),
            Element(26, "Fe", "Iron", 4, 8, ElementCategory.TRANSITION_METAL),
            Element(27, "Co", "Cobalt", 4, 9, ElementCategory.TRANSITION_METAL),
            Element(28, "Ni", "Nickel", 4, 10, ElementCategory.TRANSITION_METAL),
            Element(29, "Cu", "Copper", 4, 11, ElementCategory.TRANSITION_METAL),
            Element(30, "Zn", "Zinc", 4, 12, ElementCategory.TRANSITION_METAL),
            Element(31, "Ga", "Gallium", 4, 13, ElementCategory.POST_TRANSITION_METAL),
            Element(32, "Ge", "Germanium", 4, 14, ElementCategory.METALLOID),
            Element(33, "As", "Arsenic", 4, 15, ElementCategory.METALLOID),
            Element(34, "Se", "Selenium", 4, 16, ElementCategory.NONMETAL),
            Element(35, "Br", "Bromine", 4, 17, ElementCategory.HALOGEN),
            Element(36, "Kr", "Krypton", 4, 18, ElementCategory.NOBLE_GAS),

            // Period 5
            Element(37, "Rb", "Rubidium", 5, 1, ElementCategory.ALKALI_METAL),
            Element(38, "Sr", "Strontium", 5, 2, ElementCategory.ALKALINE_EARTH_METAL),
            Element(39, "Y", "Yttrium", 5, 3, ElementCategory.TRANSITION_METAL),
            Element(40, "Zr", "Zirconium", 5, 4, ElementCategory.TRANSITION_METAL),
            Element(41, "Nb", "Niobium", 5, 5, ElementCategory.TRANSITION_METAL),
            Element(42, "Mo", "Molybdenum", 5, 6, ElementCategory.TRANSITION_METAL),
            Element(43, "Tc", "Technetium", 5, 7, ElementCategory.TRANSITION_METAL),
            Element(44, "Ru", "Ruthenium", 5, 8, ElementCategory.TRANSITION_METAL),
            Element(45, "Rh", "Rhodium", 5, 9, ElementCategory.TRANSITION_METAL),
            Element(46, "Pd", "Palladium", 5, 10, ElementCategory.TRANSITION_METAL),
            Element(47, "Ag", "Silver", 5, 11, ElementCategory.TRANSITION_METAL),
            Element(48, "Cd", "Cadmium", 5, 12, ElementCategory.TRANSITION_METAL),
            Element(49, "In", "Indium", 5, 13, ElementCategory.POST_TRANSITION_METAL),
            Element(50, "Sn", "Tin", 5, 14, ElementCategory.POST_TRANSITION_METAL),
            Element(51, "Sb", "Antimony", 5, 15, ElementCategory.METALLOID),
            Element(52, "Te", "Tellurium", 5, 16, ElementCategory.METALLOID),
            Element(53, "I", "Iodine", 5, 17, ElementCategory.HALOGEN),
            Element(54, "Xe", "Xenon", 5, 18, ElementCategory.NOBLE_GAS),

            // Period 6
            Element(55, "Cs", "Cesium", 6, 1, ElementCategory.ALKALI_METAL),
            Element(56, "Ba", "Barium", 6, 2, ElementCategory.ALKALINE_EARTH_METAL),
            Element(57, "La", "Lanthanum", 6, 3, ElementCategory.LANTHANIDE),
            Element(72, "Hf", "Hafnium", 6, 4, ElementCategory.TRANSITION_METAL),
            Element(73, "Ta", "Tantalum", 6, 5, ElementCategory.TRANSITION_METAL),
            Element(74, "W", "Tungsten", 6, 6, ElementCategory.TRANSITION_METAL),
            Element(75, "Re", "Rhenium", 6, 7, ElementCategory.TRANSITION_METAL),
            Element(76, "Os", "Osmium", 6, 8, ElementCategory.TRANSITION_METAL),
            Element(77, "Ir", "Iridium", 6, 9, ElementCategory.TRANSITION_METAL),
            Element(78, "Pt", "Platinum", 6, 10, ElementCategory.TRANSITION_METAL),
            Element(79, "Au", "Gold", 6, 11, ElementCategory.TRANSITION_METAL),
            Element(80, "Hg", "Mercury", 6, 12, ElementCategory.TRANSITION_METAL),
            Element(81, "Tl", "Thallium", 6, 13, ElementCategory.POST_TRANSITION_METAL),
            Element(82, "Pb", "Lead", 6, 14, ElementCategory.POST_TRANSITION_METAL),
            Element(83, "Bi", "Bismuth", 6, 15, ElementCategory.POST_TRANSITION_METAL),
            Element(84, "Po", "Polonium", 6, 16, ElementCategory.POST_TRANSITION_METAL),
            Element(85, "At", "Astatine", 6, 17, ElementCategory.HALOGEN),
            Element(86, "Rn", "Radon", 6, 18, ElementCategory.NOBLE_GAS),

            // Period 7
            Element(87, "Fr", "Francium", 7, 1, ElementCategory.ALKALI_METAL),
            Element(88, "Ra", "Radium", 7, 2, ElementCategory.ALKALINE_EARTH_METAL),
            Element(89, "Ac", "Actinium", 7, 3, ElementCategory.ACTINIDE),
            Element(104, "Rf", "Rutherfordium", 7, 4, ElementCategory.TRANSITION_METAL),
            Element(105, "Db", "Dubnium", 7, 5, ElementCategory.TRANSITION_METAL),
            Element(106, "Sg", "Seaborgium", 7, 6, ElementCategory.TRANSITION_METAL),
            Element(107, "Bh", "Bohrium", 7, 7, ElementCategory.TRANSITION_METAL),
            Element(108, "Hs", "Hassium", 7, 8, ElementCategory.TRANSITION_METAL),
            Element(109, "Mt", "Meitnerium", 7, 9, ElementCategory.TRANSITION_METAL),
            Element(110, "Ds", "Darmstadtium", 7, 10, ElementCategory.TRANSITION_METAL),
            Element(111, "Rg", "Roentgenium", 7, 11, ElementCategory.TRANSITION_METAL),
            Element(112, "Cn", "Copernicium", 7, 12, ElementCategory.TRANSITION_METAL),
            Element(113, "Nh", "Nihonium", 7, 13, ElementCategory.POST_TRANSITION_METAL),
            Element(114, "Fl", "Flerovium", 7, 14, ElementCategory.POST_TRANSITION_METAL),
            Element(115, "Mc", "Moscovium", 7, 15, ElementCategory.POST_TRANSITION_METAL),
            Element(116, "Lv", "Livermorium", 7, 16, ElementCategory.POST_TRANSITION_METAL),
            Element(117, "Ts", "Tennessine", 7, 17, ElementCategory.HALOGEN),
            Element(118, "Og", "Oganesson", 7, 18, ElementCategory.NOBLE_GAS),

            // Lanthanides (row 8)
            Element(58, "Ce", "Cerium", 8, 4, ElementCategory.LANTHANIDE),
            Element(59, "Pr", "Praseodymium", 8, 5, ElementCategory.LANTHANIDE),
            Element(60, "Nd", "Neodymium", 8, 6, ElementCategory.LANTHANIDE),
            Element(61, "Pm", "Promethium", 8, 7, ElementCategory.LANTHANIDE),
            Element(62, "Sm", "Samarium", 8, 8, ElementCategory.LANTHANIDE),
            Element(63, "Eu", "Europium", 8, 9, ElementCategory.LANTHANIDE),
            Element(64, "Gd", "Gadolinium", 8, 10, ElementCategory.LANTHANIDE),
            Element(65, "Tb", "Terbium", 8, 11, ElementCategory.LANTHANIDE),
            Element(66, "Dy", "Dysprosium", 8, 12, ElementCategory.LANTHANIDE),
            Element(67, "Ho", "Holmium", 8, 13, ElementCategory.LANTHANIDE),
            Element(68, "Er", "Erbium", 8, 14, ElementCategory.LANTHANIDE),
            Element(69, "Tm", "Thulium", 8, 15, ElementCategory.LANTHANIDE),
            Element(70, "Yb", "Ytterbium", 8, 16, ElementCategory.LANTHANIDE),
            Element(71, "Lu", "Lutetium", 8, 17, ElementCategory.LANTHANIDE),

            // Actinides (row 9)
            Element(90, "Th", "Thorium", 9, 4, ElementCategory.ACTINIDE),
            Element(91, "Pa", "Protactinium", 9, 5, ElementCategory.ACTINIDE),
            Element(92, "U", "Uranium", 9, 6, ElementCategory.ACTINIDE),
            Element(93, "Np", "Neptunium", 9, 7, ElementCategory.ACTINIDE),
            Element(94, "Pu", "Plutonium", 9, 8, ElementCategory.ACTINIDE),
            Element(95, "Am", "Americium", 9, 9, ElementCategory.ACTINIDE),
            Element(96, "Cm", "Curium", 9, 10, ElementCategory.ACTINIDE),
            Element(97, "Bk", "Berkelium", 9, 11, ElementCategory.ACTINIDE),
            Element(98, "Cf", "Californium", 9, 12, ElementCategory.ACTINIDE),
            Element(99, "Es", "Einsteinium", 9, 13, ElementCategory.ACTINIDE),
            Element(100, "Fm", "Fermium", 9, 14, ElementCategory.ACTINIDE),
            Element(101, "Md", "Mendelevium", 9, 15, ElementCategory.ACTINIDE),
            Element(102, "No", "Nobelium", 9, 16, ElementCategory.ACTINIDE),
            Element(103, "Lr", "Lawrencium", 9, 17, ElementCategory.ACTINIDE)
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
