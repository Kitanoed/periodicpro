package com.app.periodicpro

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import com.app.periodicpro.data.Element
import com.app.periodicpro.data.ElementCategory
import com.app.periodicpro.data.FavoriteElement
import com.app.periodicpro.helper.ElementListViewAdapter

class FavoritesActivity : Activity() {

    private lateinit var listOfSubjects: MutableList<FavoriteElement>
    private lateinit var arrayAdapter: ElementListViewAdapter
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val listview = findViewById<ListView>(R.id.listview)
        btnBack = findViewById(R.id.btn_back)

        // Set up back button
        btnBack.setOnClickListener {
            finish()
        }

        // Create sample data with proper element details
        listOfSubjects = mutableListOf(
            createSubjectFromElement(Element(1, "H", "Hydrogen", 1, 1, ElementCategory.NONMETAL)),
            createSubjectFromElement(Element(8, "O", "Oxygen", 2, 16, ElementCategory.NONMETAL)),
            createSubjectFromElement(Element(47, "Ag", "Silver", 5, 11, ElementCategory.TRANSITION_METAL)),
            createSubjectFromElement(Element(2, "He", "Helium", 1, 18, ElementCategory.NOBLE_GAS)),
            createSubjectFromElement(Element(79, "Au", "Gold", 6, 11, ElementCategory.TRANSITION_METAL)),
            createSubjectFromElement(Element(6, "C", "Carbon", 2, 14, ElementCategory.NONMETAL)),
            createSubjectFromElement(Element(7, "N", "Nitrogen", 2, 15, ElementCategory.NONMETAL)),
            createSubjectFromElement(Element(11, "Na", "Sodium", 3, 1, ElementCategory.ALKALI_METAL)),
        )

        arrayAdapter = ElementListViewAdapter(
            this,
            listOfSubjects,
            onClickShowMore = { subject ->
                showElementDetailsDialog(subject)
            },
            onClickItem = { subject ->
                // Simply display a toast indicating the element was clicked
                android.widget.Toast.makeText(this, "Clicked on ${subject.code}", android.widget.Toast.LENGTH_SHORT).show()
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
            otherDetails = "Period: ${element.row}\nGroup: ${element.column}\nCategory: ${formatCategory(element.category)}",
            category = element.category
        )
    }

    private fun formatCategory(category: ElementCategory): String {
        return category.name.replace("_", " ").lowercase().split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
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