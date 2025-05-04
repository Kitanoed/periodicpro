package com.app.periodicpro

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.app.periodicpro.data.Element
import com.app.periodicpro.data.ElementCategory
import com.google.android.material.button.MaterialButton
import org.json.JSONArray
import org.json.JSONObject

class PeriodicTableActivity : Activity() {
    private lateinit var gridLayout: GridLayout
    private val elements = mutableListOf<Element>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_periodic_table)

        val backBtn = findViewById<ImageView>(R.id.btn_back_table)
        backBtn.setOnClickListener {
            finish()
        }

        gridLayout = findViewById(R.id.periodicTableGrid)

        initializeElements()
        setupPeriodicTable()

        // Category filter button
        val btnShowCategories = findViewById<MaterialButton>(R.id.btnShowCategories)
        btnShowCategories.setOnClickListener {
            showCategoriesDialog()
        }
        
        // View all categories button
        val btnViewAllCategories = findViewById<MaterialButton>(R.id.btnViewAllCategories)
        btnViewAllCategories.setOnClickListener {
            showCategoriesDialog()
        }
    }

    private fun showCategoriesDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_element_categories)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val closeBtn = dialog.findViewById<MaterialButton>(R.id.btnCloseDialog)
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showElementInfoDialog(element: Element) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.element_popup)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Set element symbol
        val symbolView = dialog.findViewById<TextView>(R.id.popup_symbol)
        symbolView.text = element.symbol
        
        // Set element background color based on category
        val bgResId = when (element.category) {
            ElementCategory.ALKALI_METAL -> R.drawable.rounded_alkali_metal_bg
            ElementCategory.ALKALINE_EARTH_METAL -> R.drawable.rounded_alkaline_earth_bg
            ElementCategory.TRANSITION_METAL -> R.drawable.rounded_transition_bg
            ElementCategory.POST_TRANSITION_METAL -> R.drawable.rounded_post_transition_bg
            ElementCategory.METALLOID -> R.drawable.rounded_metalloid_bg
            ElementCategory.NONMETAL -> R.drawable.rounded_nonmetal_bg
            ElementCategory.HALOGEN -> R.drawable.rounded_halogen_bg
            ElementCategory.NOBLE_GAS -> R.drawable.rounded_noble_gas_bg
            ElementCategory.LANTHANIDE -> R.drawable.rounded_lanthanide_bg
            ElementCategory.ACTINIDE -> R.drawable.rounded_actinide_bg
        }
        symbolView.setBackgroundResource(bgResId)

        // Set element details
        dialog.findViewById<TextView>(R.id.popup_name).text = "${element.name} (${element.symbol})"
        dialog.findViewById<TextView>(R.id.popup_atomic_number).text = element.atomicNumber.toString()
        dialog.findViewById<TextView>(R.id.popup_atomic_mass).text = element.atomicMass.toString()
        dialog.findViewById<TextView>(R.id.popup_row).text = element.row.toString()
        dialog.findViewById<TextView>(R.id.popup_column).text = element.column.toString()
        dialog.findViewById<TextView>(R.id.popup_category).text = 
            element.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }

        // Close button
        dialog.findViewById<MaterialButton>(R.id.popup_close_button).setOnClickListener {
            dialog.dismiss()
        }
        
        // See More button
        dialog.findViewById<MaterialButton>(R.id.popup_see_more_button).setOnClickListener {
            Toast.makeText(this, "Opening details for ${element.name}...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ElementDetailsActivity::class.java)
            intent.putExtra(ElementDetailsActivity.EXTRA_ELEMENT_ATOMIC_NUMBER, element.atomicNumber)
            startActivity(intent)
            dialog.dismiss()
        }
        
        // Add to favorites button
        val favoriteIcon = dialog.findViewById<ImageView>(R.id.popup_add_favorites)
        
        // Check if element is already in favorites
        val sharedPref = getSharedPreferences("Favorites", MODE_PRIVATE)
        val count = sharedPref.getInt("count", 0)
        var isInFavorites = false
        
        for (i in 0 until count) {
            if (sharedPref.getString("name_$i", "") == element.name) {
                isInFavorites = true
                break
            }
        }
        
        // Set initial icon state
        if (isInFavorites) {
            favoriteIcon.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            favoriteIcon.setImageResource(android.R.drawable.btn_star_big_off)
        }
        
        favoriteIcon.setOnClickListener {
            if (isInFavorites) {
                // Remove from favorites
                removeFromFavorites(element.name)
                favoriteIcon.setImageResource(android.R.drawable.btn_star_big_off)
                isInFavorites = false
                Toast.makeText(this, "${element.name} removed from favorites!", Toast.LENGTH_SHORT).show()
            } else {
                saveToFavorites(element)
                favoriteIcon.setImageResource(android.R.drawable.btn_star_big_on)
                isInFavorites = true
                Toast.makeText(this, "${element.name} added to favorites!", Toast.LENGTH_SHORT).show()
            }
        }

        // Save to recent when showing element details
        saveToRecent(element)

        dialog.show()
    }

    private fun saveToRecent(element: Element) {
        val prefs = getSharedPreferences("RecentElements", MODE_PRIVATE)
        val listJson = JSONArray(prefs.getString("recent", "[]"))
        val newObj = JSONObject().apply {
            put("name", element.name)
            put("symbol", element.symbol)
            put("number", element.atomicNumber)
        }

        val updated = JSONArray()
        updated.put(newObj)
        for (i in 0 until listJson.length()) {
            val item = listJson.getJSONObject(i)
            if (item.getString("name") != element.name) {
                updated.put(item)
            }
        }

        while (updated.length() > 5) {
            updated.remove(updated.length() - 1)
        }

        prefs.edit().putString("recent", updated.toString()).apply()
    }

    private fun saveToFavorites(element: Element) {
        val sharedPref = getSharedPreferences("Favorites", MODE_PRIVATE)
        val count = sharedPref.getInt("count", 0)
        
        // Check if already in favorites
        for (i in 0 until count) {
            if (sharedPref.getString("name_$i", "") == element.name) {
                return
            }
        }
        
        with(sharedPref.edit()) {
            putString("name_$count", element.name)
            putString("symbol_$count", element.symbol)
            putInt("number_$count", element.atomicNumber)
            putString("category_$count", element.category.name)
            putInt("count", count + 1)
            apply()
        }
    }

    private fun removeFromFavorites(elementName: String) {
        val sharedPref = getSharedPreferences("Favorites", MODE_PRIVATE)
        val count = sharedPref.getInt("count", 0)
        
        // Create a new list without the element to remove
        val updatedFavorites = mutableListOf<Triple<String, String, Int>>()
        
        for (i in 0 until count) {
            val name = sharedPref.getString("name_$i", null) ?: continue
            val symbol = sharedPref.getString("symbol_$i", null) ?: continue
            val number = sharedPref.getInt("number_$i", -1)
            val categoryStr = sharedPref.getString("category_$i", null) ?: continue
            
            if (name != elementName) {
                updatedFavorites.add(Triple(name, symbol, number))
            }
        }
        
        // Clear existing favorites
        val editor = sharedPref.edit()
        editor.clear()
        
        // Save updated favorites list
        for (i in updatedFavorites.indices) {
            val (name, symbol, number) = updatedFavorites[i]
            editor.putString("name_$i", name)
            editor.putString("symbol_$i", symbol)
            editor.putInt("number_$i", number)
            
            // Try to find the category of this element
            val element = elements.find { it.name == name }
            if (element != null) {
                editor.putString("category_$i", element.category.name)
            }
        }
        
        editor.putInt("count", updatedFavorites.size)
        editor.apply()
    }

    private fun setupPeriodicTable() {
        try {
            gridLayout.columnCount = 18
            gridLayout.rowCount = 9
            gridLayout.useDefaultMargins = true

            val cellWidth = try {
                resources.getDimensionPixelSize(R.dimen.element_card_width)
            } catch (e: Exception) {
                60
            }

            val cellHeight = try {
                resources.getDimensionPixelSize(R.dimen.element_card_height)
            } catch (e: Exception) {
                60
            }

            for (element in elements) {
                val view = createElementView(element)
                val params = GridLayout.LayoutParams().apply {
                    width = cellWidth
                    height = cellHeight
                    rowSpec = GridLayout.spec(element.row - 1)
                    columnSpec = GridLayout.spec(element.column - 1)
                    setMargins(4, 4, 4, 4)
                }
                gridLayout.addView(view, params)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error setting up periodic table: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun createElementView(element: Element): View {
        val view = LayoutInflater.from(this).inflate(R.layout.element_card, null)

        view.findViewById<TextView>(R.id.atomicNumber).text = element.atomicNumber.toString()
        view.findViewById<TextView>(R.id.elementSymbol).text = element.symbol
        view.findViewById<TextView>(R.id.elementName).text = element.name

        val bgResId = when (element.category) {
            ElementCategory.ALKALI_METAL -> R.drawable.rounded_alkali_metal_bg
            ElementCategory.ALKALINE_EARTH_METAL -> R.drawable.rounded_alkaline_earth_bg
            ElementCategory.TRANSITION_METAL -> R.drawable.rounded_transition_bg
            ElementCategory.POST_TRANSITION_METAL -> R.drawable.rounded_post_transition_bg
            ElementCategory.METALLOID -> R.drawable.rounded_metalloid_bg
            ElementCategory.NONMETAL -> R.drawable.rounded_nonmetal_bg
            ElementCategory.HALOGEN -> R.drawable.rounded_halogen_bg
            ElementCategory.NOBLE_GAS -> R.drawable.rounded_noble_gas_bg
            ElementCategory.LANTHANIDE -> R.drawable.rounded_lanthanide_bg
            ElementCategory.ACTINIDE -> R.drawable.rounded_actinide_bg
        }

        view.setBackgroundResource(bgResId)
        view.contentDescription = "${element.name}, element ${element.atomicNumber}, symbol ${element.symbol}"

        view.setOnClickListener {
            showElementInfoDialog(element)
        }

        return view
    }

    private fun initializeElements() {
        elements.clear()

        elements.addAll(
            listOf(
                // Period 1
                Element(1, "H", "Hydrogen", 1, 1, ElementCategory.NONMETAL, 1.008),
                Element(2, "He", "Helium", 1, 18, ElementCategory.NOBLE_GAS, 4.0026),

                // Period 2
                Element(3, "Li", "Lithium", 2, 1, ElementCategory.ALKALI_METAL, 6.94),
                Element(4, "Be", "Beryllium", 2, 2, ElementCategory.ALKALINE_EARTH_METAL, 9.0122),
                Element(5, "B", "Boron", 2, 13, ElementCategory.METALLOID, 10.81),
                Element(6, "C", "Carbon", 2, 14, ElementCategory.NONMETAL, 12.011),
                Element(7, "N", "Nitrogen", 2, 15, ElementCategory.NONMETAL, 14.007),
                Element(8, "O", "Oxygen", 2, 16, ElementCategory.NONMETAL, 15.999),
                Element(9, "F", "Fluorine", 2, 17, ElementCategory.HALOGEN, 18.998),
                Element(10, "Ne", "Neon", 2, 18, ElementCategory.NOBLE_GAS, 20.180),

                // Period 3
                Element(11, "Na", "Sodium", 3, 1, ElementCategory.ALKALI_METAL, 22.990),
                Element(12, "Mg", "Magnesium", 3, 2, ElementCategory.ALKALINE_EARTH_METAL, 24.305),
                Element(13, "Al", "Aluminum", 3, 13, ElementCategory.POST_TRANSITION_METAL, 26.982),
                Element(14, "Si", "Silicon", 3, 14, ElementCategory.METALLOID, 28.085),
                Element(15, "P", "Phosphorus", 3, 15, ElementCategory.NONMETAL, 30.974),
                Element(16, "S", "Sulfur", 3, 16, ElementCategory.NONMETAL, 32.06),
                Element(17, "Cl", "Chlorine", 3, 17, ElementCategory.HALOGEN, 35.45),
                Element(18, "Ar", "Argon", 3, 18, ElementCategory.NOBLE_GAS, 39.948),

                // Period 4
                Element(19, "K", "Potassium", 4, 1, ElementCategory.ALKALI_METAL, 39.098),
                Element(20, "Ca", "Calcium", 4, 2, ElementCategory.ALKALINE_EARTH_METAL, 40.078),
                Element(21, "Sc", "Scandium", 4, 3, ElementCategory.TRANSITION_METAL, 44.956),
                Element(22, "Ti", "Titanium", 4, 4, ElementCategory.TRANSITION_METAL, 47.867),
                Element(23, "V", "Vanadium", 4, 5, ElementCategory.TRANSITION_METAL, 50.942),
                Element(24, "Cr", "Chromium", 4, 6, ElementCategory.TRANSITION_METAL, 51.996),
                Element(25, "Mn", "Manganese", 4, 7, ElementCategory.TRANSITION_METAL, 54.938),
                Element(26, "Fe", "Iron", 4, 8, ElementCategory.TRANSITION_METAL, 55.845),
                Element(27, "Co", "Cobalt", 4, 9, ElementCategory.TRANSITION_METAL, 58.933),
                Element(28, "Ni", "Nickel", 4, 10, ElementCategory.TRANSITION_METAL, 58.693),
                Element(29, "Cu", "Copper", 4, 11, ElementCategory.TRANSITION_METAL, 63.546),
                Element(30, "Zn", "Zinc", 4, 12, ElementCategory.TRANSITION_METAL, 65.38),
                Element(31, "Ga", "Gallium", 4, 13, ElementCategory.POST_TRANSITION_METAL, 69.723),
                Element(32, "Ge", "Germanium", 4, 14, ElementCategory.METALLOID, 72.630),
                Element(33, "As", "Arsenic", 4, 15, ElementCategory.METALLOID, 74.922),
                Element(34, "Se", "Selenium", 4, 16, ElementCategory.NONMETAL, 78.971),
                Element(35, "Br", "Bromine", 4, 17, ElementCategory.HALOGEN, 79.904),
                Element(36, "Kr", "Krypton", 4, 18, ElementCategory.NOBLE_GAS, 83.798),

                // Period 5
                Element(37, "Rb", "Rubidium", 5, 1, ElementCategory.ALKALI_METAL, 85.468),
                Element(38, "Sr", "Strontium", 5, 2, ElementCategory.ALKALINE_EARTH_METAL, 87.62),
                Element(39, "Y", "Yttrium", 5, 3, ElementCategory.TRANSITION_METAL, 88.906),
                Element(40, "Zr", "Zirconium", 5, 4, ElementCategory.TRANSITION_METAL, 91.224),
                Element(41, "Nb", "Niobium", 5, 5, ElementCategory.TRANSITION_METAL, 92.906),
                Element(42, "Mo", "Molybdenum", 5, 6, ElementCategory.TRANSITION_METAL, 95.95),
                Element(43, "Tc", "Technetium", 5, 7, ElementCategory.TRANSITION_METAL, 98.0),
                Element(44, "Ru", "Ruthenium", 5, 8, ElementCategory.TRANSITION_METAL, 101.07),
                Element(45, "Rh", "Rhodium", 5, 9, ElementCategory.TRANSITION_METAL, 102.91),
                Element(46, "Pd", "Palladium", 5, 10, ElementCategory.TRANSITION_METAL, 106.42),
                Element(47, "Ag", "Silver", 5, 11, ElementCategory.TRANSITION_METAL, 107.87),
                Element(48, "Cd", "Cadmium", 5, 12, ElementCategory.TRANSITION_METAL, 112.41),
                Element(49, "In", "Indium", 5, 13, ElementCategory.POST_TRANSITION_METAL, 114.82),
                Element(50, "Sn", "Tin", 5, 14, ElementCategory.POST_TRANSITION_METAL, 118.71),
                Element(51, "Sb", "Antimony", 5, 15, ElementCategory.METALLOID, 121.76),
                Element(52, "Te", "Tellurium", 5, 16, ElementCategory.METALLOID, 127.60),
                Element(53, "I", "Iodine", 5, 17, ElementCategory.HALOGEN, 126.90),
                Element(54, "Xe", "Xenon", 5, 18, ElementCategory.NOBLE_GAS, 131.29),

                // Period 6
                Element(55, "Cs", "Cesium", 6, 1, ElementCategory.ALKALI_METAL, 132.91),
                Element(56, "Ba", "Barium", 6, 2, ElementCategory.ALKALINE_EARTH_METAL, 137.33),
                Element(57, "La", "Lanthanum", 6, 3, ElementCategory.LANTHANIDE, 138.91),
                Element(72, "Hf", "Hafnium", 6, 4, ElementCategory.TRANSITION_METAL, 178.49),
                Element(73, "Ta", "Tantalum", 6, 5, ElementCategory.TRANSITION_METAL, 180.95),
                Element(74, "W", "Tungsten", 6, 6, ElementCategory.TRANSITION_METAL, 183.84),
                Element(75, "Re", "Rhenium", 6, 7, ElementCategory.TRANSITION_METAL, 186.21),
                Element(76, "Os", "Osmium", 6, 8, ElementCategory.TRANSITION_METAL, 190.23),
                Element(77, "Ir", "Iridium", 6, 9, ElementCategory.TRANSITION_METAL, 192.22),
                Element(78, "Pt", "Platinum", 6, 10, ElementCategory.TRANSITION_METAL, 195.08),
                Element(79, "Au", "Gold", 6, 11, ElementCategory.TRANSITION_METAL, 196.97),
                Element(80, "Hg", "Mercury", 6, 12, ElementCategory.TRANSITION_METAL, 200.59),
                Element(81, "Tl", "Thallium", 6, 13, ElementCategory.POST_TRANSITION_METAL, 204.38),
                Element(82, "Pb", "Lead", 6, 14, ElementCategory.POST_TRANSITION_METAL, 207.2),
                Element(83, "Bi", "Bismuth", 6, 15, ElementCategory.POST_TRANSITION_METAL, 208.98),
                Element(84, "Po", "Polonium", 6, 16, ElementCategory.POST_TRANSITION_METAL, 209.0),
                Element(85, "At", "Astatine", 6, 17, ElementCategory.HALOGEN, 210.0),
                Element(86, "Rn", "Radon", 6, 18, ElementCategory.NOBLE_GAS, 222.0),

                // Period 7
                Element(87, "Fr", "Francium", 7, 1, ElementCategory.ALKALI_METAL, 223.0),
                Element(88, "Ra", "Radium", 7, 2, ElementCategory.ALKALINE_EARTH_METAL, 226.0),
                Element(89, "Ac", "Actinium", 7, 3, ElementCategory.ACTINIDE, 227.0),
                Element(104, "Rf", "Rutherfordium", 7, 4, ElementCategory.TRANSITION_METAL, 267.0),
                Element(105, "Db", "Dubnium", 7, 5, ElementCategory.TRANSITION_METAL, 270.0),
                Element(106, "Sg", "Seaborgium", 7, 6, ElementCategory.TRANSITION_METAL, 269.0),
                Element(107, "Bh", "Bohrium", 7, 7, ElementCategory.TRANSITION_METAL, 270.0),
                Element(108, "Hs", "Hassium", 7, 8, ElementCategory.TRANSITION_METAL, 277.0),
                Element(109, "Mt", "Meitnerium", 7, 9, ElementCategory.TRANSITION_METAL, 278.0),
                Element(110, "Ds", "Darmstadtium", 7, 10, ElementCategory.TRANSITION_METAL, 281.0),
                Element(111, "Rg", "Roentgenium", 7, 11, ElementCategory.TRANSITION_METAL, 282.0),
                Element(112, "Cn", "Copernicium", 7, 12, ElementCategory.TRANSITION_METAL, 285.0),
                Element(113, "Nh", "Nihonium", 7, 13, ElementCategory.POST_TRANSITION_METAL, 286.0),
                Element(114, "Fl", "Flerovium", 7, 14, ElementCategory.POST_TRANSITION_METAL, 289.0),
                Element(115, "Mc", "Moscovium", 7, 15, ElementCategory.POST_TRANSITION_METAL, 289.0),
                Element(116, "Lv", "Livermorium", 7, 16, ElementCategory.POST_TRANSITION_METAL, 293.0),
                Element(117, "Ts", "Tennessine", 7, 17, ElementCategory.HALOGEN, 293.0),
                Element(118, "Og", "Oganesson", 7, 18, ElementCategory.NOBLE_GAS, 294.0),

                // Lanthanides (row 8)
                Element(58, "Ce", "Cerium", 8, 4, ElementCategory.LANTHANIDE, 140.12),
                Element(59, "Pr", "Praseodymium", 8, 5, ElementCategory.LANTHANIDE, 140.91),
                Element(60, "Nd", "Neodymium", 8, 6, ElementCategory.LANTHANIDE, 144.24),
                Element(61, "Pm", "Promethium", 8, 7, ElementCategory.LANTHANIDE, 145.0),
                Element(62, "Sm", "Samarium", 8, 8, ElementCategory.LANTHANIDE, 150.36),
                Element(63, "Eu", "Europium", 8, 9, ElementCategory.LANTHANIDE, 151.96),
                Element(64, "Gd", "Gadolinium", 8, 10, ElementCategory.LANTHANIDE, 157.25),
                Element(65, "Tb", "Terbium", 8, 11, ElementCategory.LANTHANIDE, 158.93),
                Element(66, "Dy", "Dysprosium", 8, 12, ElementCategory.LANTHANIDE, 162.50),
                Element(67, "Ho", "Holmium", 8, 13, ElementCategory.LANTHANIDE, 164.93),
                Element(68, "Er", "Erbium", 8, 14, ElementCategory.LANTHANIDE, 167.26),
                Element(69, "Tm", "Thulium", 8, 15, ElementCategory.LANTHANIDE, 168.93),
                Element(70, "Yb", "Ytterbium", 8, 16, ElementCategory.LANTHANIDE, 173.05),
                Element(71, "Lu", "Lutetium", 8, 17, ElementCategory.LANTHANIDE, 174.97),

                // Actinides (row 9)
                Element(90, "Th", "Thorium", 9, 4, ElementCategory.ACTINIDE, 232.04),
                Element(91, "Pa", "Protactinium", 9, 5, ElementCategory.ACTINIDE, 231.04),
                Element(92, "U", "Uranium", 9, 6, ElementCategory.ACTINIDE, 238.03),
                Element(93, "Np", "Neptunium", 9, 7, ElementCategory.ACTINIDE, 237.0),
                Element(94, "Pu", "Plutonium", 9, 8, ElementCategory.ACTINIDE, 244.0),
                Element(95, "Am", "Americium", 9, 9, ElementCategory.ACTINIDE, 243.0),
                Element(96, "Cm", "Curium", 9, 10, ElementCategory.ACTINIDE, 247.0),
                Element(97, "Bk", "Berkelium", 9, 11, ElementCategory.ACTINIDE, 247.0),
                Element(98, "Cf", "Californium", 9, 12, ElementCategory.ACTINIDE, 251.0),
                Element(99, "Es", "Einsteinium", 9, 13, ElementCategory.ACTINIDE, 252.0),
                Element(100, "Fm", "Fermium", 9, 14, ElementCategory.ACTINIDE, 257.0),
                Element(101, "Md", "Mendelevium", 9, 15, ElementCategory.ACTINIDE, 258.0),
                Element(102, "No", "Nobelium", 9, 16, ElementCategory.ACTINIDE, 259.0),
                Element(103, "Lr", "Lawrencium", 9, 17, ElementCategory.ACTINIDE, 262.0)
            )
        )
    }
}
