package com.app.periodicpro.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.periodicpro.R
import com.app.periodicpro.data.ElementCategory
import com.app.periodicpro.data.FavoriteElement

class ElementListViewAdapter(
    private val context: Context,
    private val subjectList: List<FavoriteElement>,
    private val onClickShowMore: (FavoriteElement) -> Unit,
    private val onClickItem: (FavoriteElement) -> Unit,
    private val onLongPressed: (position: Int) -> Unit
): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.element_list,
            parent,
            false
        )

        val symbol = view.findViewById<TextView>(R.id.textview_symbol)
        val code = view.findViewById<TextView>(R.id.textview_code)
        val desc = view.findViewById<TextView>(R.id.textview_desc)
        val schedule = view.findViewById<TextView>(R.id.textview_schedule)
        val showMore = view.findViewById<TextView>(R.id.textview_showmore)

        val subject = subjectList[position]

        // Set element symbol (first letter of name or first two characters of symbol if available)
        if (subject.desc.startsWith("Symbol:")) {
            // Extract the element symbol from the description
            val symbolText = subject.desc.substringAfter("Symbol: ").trim()
            symbol.text = symbolText
        } else {
            // Fallback to first letter of the element name
            symbol.text = subject.code.take(1)
        }

        // Set the background of the symbol TextView based on the element category
        subject.category?.let { category ->
            symbol.setBackgroundResource(getCategoryBackgroundResource(category))
        }

        code.text = subject.code
        desc.text = subject.desc
        schedule.text = subject.schedule

        showMore.setOnClickListener {
            onClickShowMore(subject)
        }

        view.setOnClickListener {
            onClickItem(subject)
        }

        view.setOnLongClickListener {
            onLongPressed(position)
            true
        }

        return view
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

    override fun getCount(): Int = subjectList.size

    override fun getItem(position: Int): Any = subjectList[position]

    override fun getItemId(position: Int): Long = position.toLong()
}