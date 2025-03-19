package com.app.periodicpro.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.app.periodicpro.R
import com.app.periodicpro.data.Subject

class ElementListViewAdapter(
    val context: Context,
    val subjectList: List<Subject>,
    val onClickShowMore : (Subject) -> Unit,
    val onClickItem: (Subject) -> Unit,
    val onLongPressed: (position: Int) -> Unit
): BaseAdapter() {

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {
        val view = contentView ?: LayoutInflater.from(context).inflate(
            R.layout.element_list,
            parent,
            false
        )

        val photo = view.findViewById<ImageView>(R.id.imageview_logo)
        val code = view.findViewById<TextView>(R.id.textview_code)
        val desc = view.findViewById<TextView>(R.id.textview_desc)
        val schedule = view.findViewById<TextView>(R.id.textview_schedule)
        val showMore = view.findViewById<TextView>(R.id.textview_showmore)

        val subject = subjectList[position]

        photo.setImageResource(subject.photo)
        code.setText(subject.code)
        desc.setText(subject.desc)
        schedule.setText(subject.schedule)

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

    override fun getCount(): Int = subjectList.size

    override fun getItem(position: Int): Any = subjectList[position]

    override fun getItemId(position: Int): Long = position.toLong()
}