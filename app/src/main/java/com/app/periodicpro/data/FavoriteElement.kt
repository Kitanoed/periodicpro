package com.app.periodicpro.data

import com.app.periodicpro.R

data class FavoriteElement(
    var photo: Int = R.drawable.logo,
    var code: String = "",
    var desc: String = "",
    var schedule: String = "",
    var otherDetails: String = "",
    var category: ElementCategory? = null
)