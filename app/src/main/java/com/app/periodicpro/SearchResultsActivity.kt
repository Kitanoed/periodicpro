package com.app.periodicpro

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.app.periodicpro.data.Element
import com.app.periodicpro.data.ElementCategory

class SearchResultsActivity : Activity() {
    private lateinit var lvSearchResults: ListView
    private lateinit var tvResultsCount: TextView
    private lateinit var tvSearchQuery: TextView
    private lateinit var btnBack: ImageView

    private val searchResults = mutableListOf<Element>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        lvSearchResults = findViewById(R.id.lv_search_results)
        tvResultsCount = findViewById(R.id.tv_results_count)
        tvSearchQuery = findViewById(R.id.tv_search_query)
        btnBack = findViewById(R.id.btn_back)

        btnBack.setOnClickListener { finish() }

        val searchQuery = intent.getStringExtra("SEARCH_QUERY") ?: ""
        tvSearchQuery.text = "\"$searchQuery\""

        searchElements(searchQuery)

        // ✅ Hook custom adapter with "+ Favorite" button
        lvSearchResults.adapter = SearchResultsAdapter(searchResults)

        val resultsText = when (searchResults.size) {
            0 -> "No results found"
            1 -> "1 result found"
            else -> "${searchResults.size} results found"
        }
        tvResultsCount.text = resultsText
    }

    override fun onBackPressed() {
        finish()
    }

    // ✅ Custom Adapter Class
    inner class SearchResultsAdapter(private val elements: List<Element>) :
        ArrayAdapter<Element>(this@SearchResultsActivity, R.layout.search_result_item, elements) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.search_result_item, parent, false)
            val element = elements[position]

            val text = view.findViewById<TextView>(R.id.text_element_info)
            val btnAdd = view.findViewById<Button>(R.id.button_add_favorite)

            text.text = "${element.name} (${element.symbol}) - Atomic Number: ${element.atomicNumber}"

            btnAdd.setOnClickListener {
                saveToFavorites(element)
                Toast.makeText(
                    this@SearchResultsActivity,
                    "${element.name} added to favorites!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            return view
        }
    }

    // ✅ Save favorite using SharedPreferences (simple key-value format)
    private fun saveToFavorites(element: Element) {
        val sharedPref = getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val count = sharedPref.getInt("count", 0)

        for (i in 0 until count) {
            if (sharedPref.getString("name_$i", "") == element.name) return
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

    private fun searchElements(query: String) {
        searchResults.clear()

        val allElements = getAllElements()
        val term = query.trim().lowercase()

        searchResults.addAll(
            allElements.filter {
                it.name.lowercase().contains(term) ||
                        it.symbol.lowercase().contains(term) ||
                        it.atomicNumber.toString() == term
            }
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
            
            // Lanthanides (Period 6)
            Element(57, "La", "Lanthanum", 6, 3, ElementCategory.LANTHANIDE),
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
            
            // Period 6 (continued)
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
            
            // Actinides (Period 7)
            Element(89, "Ac", "Actinium", 7, 3, ElementCategory.ACTINIDE),
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
            Element(103, "Lr", "Lawrencium", 9, 17, ElementCategory.ACTINIDE),
            
            // Period 7 (continued)
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
            Element(118, "Og", "Oganesson", 7, 18, ElementCategory.NOBLE_GAS)
        )
    }
} 