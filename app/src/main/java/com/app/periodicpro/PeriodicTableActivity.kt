package com.app.periodicpro

import android.app.Activity
import android.app.Dialog
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

        // Category filter button (optional)
        val btnShowCategories = findViewById<Button>(R.id.btnShowCategories)
        btnShowCategories.setOnClickListener {
            showCategoriesDialog()
        }
    }

    private fun showCategoriesDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_element_categories)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val closeBtn = dialog.findViewById<Button>(R.id.btnCloseDialog)
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showElementInfoDialog(element: Element) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.element_popup)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        val nameText = dialog.findViewById<TextView>(R.id.popup_name)
        nameText.text = "${element.name} (${element.symbol})"
        dialog.findViewById<TextView>(R.id.popup_atomic_number).text = "Atomic Number: ${element.atomicNumber}"
        dialog.findViewById<TextView>(R.id.popup_row).text = "Row: ${element.row}"
        dialog.findViewById<TextView>(R.id.popup_column).text = "Column: ${element.column}"
        dialog.findViewById<TextView>(R.id.popup_category).text = 
            "Category: ${element.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}"

        dialog.findViewById<Button>(R.id.popup_close_button).setOnClickListener {
            dialog.dismiss()
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
                Element(1, "H", "Hydrogen", 1, 1, ElementCategory.NONMETAL),
                Element(2, "He", "Helium", 1, 18, ElementCategory.NOBLE_GAS),
                Element(3, "Li", "Lithium", 2, 1, ElementCategory.ALKALI_METAL),
                Element(4, "Be", "Beryllium", 2, 2, ElementCategory.ALKALINE_EARTH_METAL),
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

                // Lanthanides (row 8, columns 4-18)
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

                // Actinides (row 9, columns 4-18)
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
        )
    }
}
