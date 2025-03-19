package com.app.periodicpro

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.ListView
import com.app.periodicpro.data.Subject
import com.app.periodicpro.helper.ElementListViewAdapter

class FavoritesActivity: Activity() {

    lateinit var listOfSubjects: MutableList<Subject>
    lateinit var arrayAdapter: ElementListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val listview = findViewById<ListView>(R.id.listview)

        listOfSubjects = mutableListOf(
            Subject(R.drawable.logo, "Hydrogen", "asdfasdfsadf", "asdfsdfsdf", "asdfsdfsdf"),
            Subject(R.drawable.logo, "Oxygen", "asdfasdfsadf", "asdfsdfsdf", "asdfsdfsdf"),
            Subject(R.drawable.logo, "Silver", "asdfasdfsadf", "asdfsdfsdf", "asdfsdfsdf"),
            Subject(R.drawable.logo, "Helium", "asdfasdfsadf", "asdfsdfsdf", "asdfsdfsdf"),
            Subject(R.drawable.logo, "Hydrogen", "asdfasdfsadf", "asdfsdfsdf", "asdfsdfsdf"),
            Subject(R.drawable.logo, "Oxygen", "asdfasdfsadf", "asdfsdfsdf", "asdfsdfsdf"),
            Subject(R.drawable.logo, "Silver", "asdfasdfsadf", "asdfsdfsdf", "asdfsdfsdf"),
            Subject(R.drawable.logo, "Helium", "asdfasdfsadf", "asdfsdfsdf", "asdfsdfsdf"),
        )

        arrayAdapter = ElementListViewAdapter(
            this,
            listOfSubjects,
            onClickShowMore = { subject ->
                showOtherDetailsDialog(subject)
            },
            onClickItem = { subject ->
                listOfSubjects.add(subject)
                arrayAdapter.notifyDataSetChanged()
            },
            onLongPressed = { position ->
                showDeleteDialog(position)
            }
        )
        listview.adapter = arrayAdapter

    }

    private fun showOtherDetailsDialog(subject: Subject) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("${subject.code}")
        builder.setMessage("${subject.otherDetails}")
        builder.setPositiveButton("Okay", null)
        builder.show()
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete item")
        builder.setMessage("Are you sure to delete ${listOfSubjects[position]}")
        builder.setPositiveButton("Remove") { _, _ ->
            val removeItem = listOfSubjects.removeAt(position)
            arrayAdapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()

    }
}