package com.app.periodicpro.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.app.periodicpro.R
import com.app.periodicpro.data.Element

class SearchResultAdapter(
    private val context: Context,
    private val elements: List<Element>
) : ArrayAdapter<Element>(context, R.layout.search_result_item, elements) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false)
        val element = elements[position]

        val infoText = view.findViewById<TextView>(R.id.text_element_info)
        val btnAdd = view.findViewById<Button>(R.id.button_add_favorite)

        infoText.text = "${element.name} (${element.symbol}) - Atomic Number: ${element.atomicNumber}"

        btnAdd.setOnClickListener {
            saveToFavorites(element)
            Toast.makeText(context, "${element.name} added to favorites!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun saveToFavorites(element: Element) {
        val sharedPref = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
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
}
