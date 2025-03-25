package com.app.periodicpro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.app.Fragment
import com.app.periodicpro.R

class NavigationFragment : Fragment() {
    private var onNavigationItemSelectedListener: OnNavigationItemSelectedListener? = null

    interface OnNavigationItemSelectedListener {
        fun onNavigationItemSelected(itemId: Int)
    }

    fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener) {
        onNavigationItemSelectedListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_landing).setOnClickListener {
            onNavigationItemSelectedListener?.onNavigationItemSelected(R.id.button_landing)
        }

        view.findViewById<Button>(R.id.button_table).setOnClickListener {
            onNavigationItemSelectedListener?.onNavigationItemSelected(R.id.button_table)
        }

        view.findViewById<Button>(R.id.button_profile).setOnClickListener {
            onNavigationItemSelectedListener?.onNavigationItemSelected(R.id.button_profile)
        }

        view.findViewById<Button>(R.id.button_settings).setOnClickListener {
            onNavigationItemSelectedListener?.onNavigationItemSelected(R.id.button_settings)
        }
    }
} 