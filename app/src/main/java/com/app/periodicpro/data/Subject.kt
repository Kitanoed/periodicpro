package com.app.periodicpro.data

import com.app.periodicpro.R

data class Subject(
    var photo: Int = R.drawable.logo,
    var code: String = "",
    var desc: String = "",
    var schedule: String = "",
    var otherDetails: String = ""
)