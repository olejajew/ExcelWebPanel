package com.hedgehog.xmlparser.test

data class FirstModel(
    val id: Int,
    var title: String,
    var description: String,
    val kcalBurnedOnOneKgPerMinute: Double,
    val maleAnimPath: String,
    val femaleAnimPath: String
)