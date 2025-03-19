package com.app.periodicpro.data

enum class ElementCategory {
    ALKALI_METAL,
    ALKALINE_EARTH_METAL,
    TRANSITION_METAL,
    POST_TRANSITION_METAL,
    METALLOID,
    NONMETAL,
    HALOGEN,
    NOBLE_GAS,
    LANTHANIDE,
    ACTINIDE
}

data class Element(
    val atomicNumber: Int,
    val symbol: String,
    val name: String,
    val row: Int,
    val column: Int,
    val category: ElementCategory,
    val atomicMass: Double = 0.0,
    val electronConfiguration: String = "",
    val description: String = ""
) 