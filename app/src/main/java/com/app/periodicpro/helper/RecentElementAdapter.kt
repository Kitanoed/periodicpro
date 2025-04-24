package com.app.periodicpro.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.app.periodicpro.R
import com.app.periodicpro.data.RecentElement

class RecentElementAdapter(
    private val context: Context,
    private val recentList: List<RecentElement>
) : BaseAdapter() {

    override fun getCount(): Int = recentList.size

    override fun getItem(position: Int): Any = recentList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_recent_element, parent, false)
        val symbol = rowView.findViewById<TextView>(R.id.tv_recent_symbol)
        val name = rowView.findViewById<TextView>(R.id.tv_recent_name)
        val number = rowView.findViewById<TextView>(R.id.tv_recent_number)

        val element = recentList[position]

        symbol.text = element.name.first().toString()
        name.text = element.name
        number.text = "Atomic number: ${element.atomicNumber}"

        return rowView
    }
}
