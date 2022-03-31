package com.hedgehog.xmlparser.test

data class FirstModelCombined(
    val id: Int,
    var titleEn: String,
    var descriptionEn: String,
    var titleRu: String,
    var descriptionRu: String,
    val kcalBurnedOnOneKgPerMinute: Double,
    val maleAnimPath: String,
    val femaleAnimPath: String
)