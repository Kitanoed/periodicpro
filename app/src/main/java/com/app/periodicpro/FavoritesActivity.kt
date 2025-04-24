package com.app.periodicpro

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.app.periodicpro.data.ElementCategory
import com.app.periodicpro.data.FavoriteElement
import com.app.periodicpro.helper.ElementListViewAdapter

class FavoritesActivity : Activity() {

    private lateinit var listOfFavorites: MutableList<FavoriteElement>
    private lateinit var adapter: ElementListViewAdapter
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val listView = findViewById<ListView>(R.id.listview)
        btnBack = findViewById(R.id.btn_back)
        btnBack.setOnClickListener { finish() }

        listOfFavorites = loadFavoritesFromSharedPrefs().toMutableList()

        adapter = ElementListViewAdapter(
            this,
            listOfFavorites,
            onClickShowMore = { showElementDetailsDialog(it) },
            onClickItem = {
                Toast.makeText(this, "Clicked on ${it.code}", Toast.LENGTH_SHORT).show()
            },
            onLongPressed = { showDeleteDialog(it) }
        )

        listView.adapter = adapter
    }

    private fun loadFavoritesFromSharedPrefs(): List<FavoriteElement> {
        val sharedPref: SharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE)
        val count = sharedPref.getInt("count", 0)
        val favorites = mutableListOf<FavoriteElement>()

        for (i in 0 until count) {
            val name = sharedPref.getString("name_$i", null) ?: continue
            val symbol = sharedPref.getString("symbol_$i", "") ?: ""
            val atomicNumber = sharedPref.getInt("number_$i", 0)
            val categoryName = sharedPref.getString("category_$i", "") ?: ""
            val category = try {
                ElementCategory.valueOf(categoryName)
            } catch (e: Exception) {
                null
            }

            favorites.add(
                FavoriteElement(
                    code = name,
                    desc = "Symbol: $symbol",
                    schedule = "Atomic Number: $atomicNumber",
                    otherDetails = "",
                    category = category
                )
            )
        }

        return favorites
    }

    private fun showElementDetailsDialog(subject: FavoriteElement) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(subject.code)
        builder.setMessage(subject.otherDetails.ifEmpty { subject.desc + "\n" + subject.schedule })
        builder.setPositiveButton("Close", null)
        builder.show()
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove from Favorites")
        builder.setMessage("Remove ${listOfFavorites[position].code} from favorites?")
        builder.setPositiveButton("Remove") { _, _ ->
            listOfFavorites.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Removed from favorites.", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    override fun onBackPressed() {
        finish()
    }
}
