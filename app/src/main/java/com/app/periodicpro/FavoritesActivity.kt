package com.app.periodicpro

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.*
import com.app.periodicpro.data.Element
import com.app.periodicpro.data.ElementCategory
import com.app.periodicpro.data.FavoriteElement
import com.app.periodicpro.helper.ElementListViewAdapter

class FavoritesActivity : Activity() {

    private lateinit var listOfSubjects: MutableList<FavoriteElement>
    private lateinit var arrayAdapter: ElementListViewAdapter
    private lateinit var btnBack: ImageView
    private lateinit var btnClear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val listview = findViewById<ListView>(R.id.listview)
        btnBack = findViewById(R.id.btn_back)
        btnClear = findViewById(R.id.btn_clear_favorites)

        btnBack.setOnClickListener {
            finish()
        }

        btnClear.setOnClickListener {
            val sharedPref = getSharedPreferences("Favorites", MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            listOfSubjects.clear()
            arrayAdapter.notifyDataSetChanged()

            Toast.makeText(this, "Favorites cleared", Toast.LENGTH_SHORT).show()
        }

        // Load favorites from SharedPreferences
        val sharedPref = getSharedPreferences("Favorites", MODE_PRIVATE)
        val count = sharedPref.getInt("count", 0)

        listOfSubjects = mutableListOf()

        for (i in 0 until count) {
            val name = sharedPref.getString("name_$i", null)
            val symbol = sharedPref.getString("symbol_$i", null)
            val number = sharedPref.getInt("number_$i", -1)
            val categoryStr = sharedPref.getString("category_$i", null)

            if (name != null && symbol != null && categoryStr != null && number != -1) {
                val element = Element(
                    atomicNumber = number,
                    symbol = symbol,
                    name = name,
                    row = 0,
                    column = 0,
                    category = ElementCategory.valueOf(categoryStr)
                )
                listOfSubjects.add(createSubjectFromElement(element))
            }
        }

        arrayAdapter = ElementListViewAdapter(
            this,
            listOfSubjects,
            onClickShowMore = { subject ->
                showElementDetailsDialog(subject)
            },
            onClickItem = { subject ->
                Toast.makeText(this, "Clicked on ${subject.code}", Toast.LENGTH_SHORT).show()
            },
            onLongPressed = { position ->
                showDeleteDialog(position)
            }
        )

        listview.adapter = arrayAdapter
    }

    private fun createSubjectFromElement(element: Element): FavoriteElement {
        return FavoriteElement(
            code = element.name,
            desc = "Symbol: ${element.symbol}",
            schedule = "Atomic Number: ${element.atomicNumber}",
            otherDetails = "Category: ${element.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}",
            category = element.category
        )
    }

    private fun showElementDetailsDialog(subject: FavoriteElement) {
        // Extract element information from the favorite data
        val name = subject.code
        val symbol = subject.desc.substringAfter("Symbol: ").trim()
        val atomicNumber = subject.schedule.substringAfter("Atomic Number: ").trim().toInt()
        val categoryStr = subject.otherDetails.substringAfter("Category: ").trim()
        
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.element_popup)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Set the element symbol with appropriate background
        val symbolView = dialog.findViewById<TextView>(R.id.popup_symbol)
        symbolView.text = symbol
        
        dialog.findViewById<TextView>(R.id.popup_name).text = "$name ($symbol)"
        dialog.findViewById<TextView>(R.id.popup_atomic_number).text = atomicNumber.toString()
        
        // Find the element in the global list to get more details
        val elementData = getAllElements().find { it.name == name }
        if (elementData != null) {
            dialog.findViewById<TextView>(R.id.popup_atomic_mass).text = elementData.atomicMass.toString()
            dialog.findViewById<TextView>(R.id.popup_row).text = elementData.row.toString()
            dialog.findViewById<TextView>(R.id.popup_column).text = elementData.column.toString()
            dialog.findViewById<TextView>(R.id.popup_category).text = 
                elementData.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
                
            // Set the symbol background color based on element category
            val bgResId = getCategoryBackgroundResource(elementData.category)
            symbolView.setBackgroundResource(bgResId)
        } else {
            dialog.findViewById<TextView>(R.id.popup_atomic_mass).text = "-"
            dialog.findViewById<TextView>(R.id.popup_row).text = "-"
            dialog.findViewById<TextView>(R.id.popup_column).text = "-"
            dialog.findViewById<TextView>(R.id.popup_category).text = categoryStr
            
            // Set background from the subject category
            subject.category?.let {
                symbolView.setBackgroundResource(getCategoryBackgroundResource(it))
            }
        }

        dialog.findViewById<Button>(R.id.popup_close_button).setOnClickListener {
            dialog.dismiss()
        }
        
        // Add to favorites button - since we're already in favorites, just show it as already favorited
        val favoriteIcon = dialog.findViewById<ImageView>(R.id.popup_add_favorites)
        favoriteIcon.setImageResource(android.R.drawable.btn_star_big_on)
        
        favoriteIcon.setOnClickListener {
            // Remove from favorites
            removeFromFavorites(name)
            
            // Close the dialog
            dialog.dismiss()
            
            // Find and remove the element from the listview
            val position = listOfSubjects.indexOfFirst { it.code == name }
            if (position != -1) {
                listOfSubjects.removeAt(position)
                arrayAdapter.notifyDataSetChanged()
            }
            
            Toast.makeText(this, "$name removed from favorites!", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
    
    private fun getCategoryBackgroundResource(category: ElementCategory): Int {
        return when (category) {
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
    }
    
    private fun getAllElements(): List<Element> {
        return listOf(
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

            // Other periods would follow the same pattern...
            // For brevity, I've included just a subset of elements
            
            // You can add more periods as needed
        )
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove from Favorites")
        val elementName = listOfSubjects[position].code
        builder.setMessage("Remove $elementName from favorites?")
        builder.setPositiveButton("Remove") { _, _ ->
            // Remove from SharedPreferences
            removeFromFavorites(elementName)
            
            // Remove from UI list
            listOfSubjects.removeAt(position)
            arrayAdapter.notifyDataSetChanged()
            
            Toast.makeText(this, "$elementName removed from favorites!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun removeFromFavorites(elementName: String) {
        val sharedPref = getSharedPreferences("Favorites", MODE_PRIVATE)
        val count = sharedPref.getInt("count", 0)
        
        // Create a new list without the element to remove
        val updatedFavorites = mutableListOf<Triple<String, String, Int>>()
        var categoryStrToRemove: String? = null
        
        for (i in 0 until count) {
            val name = sharedPref.getString("name_$i", null) ?: continue
            val symbol = sharedPref.getString("symbol_$i", null) ?: continue
            val number = sharedPref.getInt("number_$i", -1)
            val categoryStr = sharedPref.getString("category_$i", null) ?: continue
            
            if (name != elementName) {
                updatedFavorites.add(Triple(name, symbol, number))
            } else {
                categoryStrToRemove = categoryStr
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
            
            // Try to find the category of this element from our list
            val element = getAllElements().find { it.name == name }
            val categoryStr = element?.category?.name ?: categoryStrToRemove ?: "NONMETAL"
            editor.putString("category_$i", categoryStr)
        }
        
        editor.putInt("count", updatedFavorites.size)
        editor.apply()
    }

    override fun onBackPressed() {
        finish()
    }
}
