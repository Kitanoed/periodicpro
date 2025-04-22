package com.app.periodicpro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView

class DeveloperActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developer)


        findViewById<ImageView>(R.id.andrei_ig).setOnClickListener {
            openLink("https://www.instagram.com/kitanoed/")
        }
        findViewById<ImageView>(R.id.andrei_fb).setOnClickListener {
            openLink("https://www.facebook.com/kiitanoed")
        }
        findViewById<ImageView>(R.id.andrei_github).setOnClickListener {
            openLink("https://github.com/Kitanoed")
        }


        findViewById<ImageView>(R.id.axcel_ig).setOnClickListener {
            openLink("https://www.instagram.com/axiiiiiiiiiil0/")
        }
        findViewById<ImageView>(R.id.axcel_fb).setOnClickListener {
            openLink("https://www.facebook.com/axcelson.macansantos/")
        }
        findViewById<ImageView>(R.id.axcel_github).setOnClickListener {
            openLink("https://github.com/AksilDev")
        }
    }

    private fun openLink(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}
