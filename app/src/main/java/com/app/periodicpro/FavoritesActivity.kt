package com.app.periodicpro

import android.app.Activity
import android.app.AlertDialog
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
        val builder = AlertDialog.Builder(this)
        builder.setTitle(subject.code)
        builder.setMessage(subject.otherDetails)
        builder.setPositiveButton("Close", null)
        builder.show()
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove from Favorites")
        builder.setMessage("Remove ${listOfSubjects[position].code} from favorites?")
        builder.setPositiveButton("Remove") { _, _ ->
            listOfSubjects.removeAt(position)
            arrayAdapter.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    override fun onBackPressed() {
        finish()
    }
}
