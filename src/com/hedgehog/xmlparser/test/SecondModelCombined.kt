package com.hedgehog.xmlparser.test

data class SecondModelCombined(
    val exerciseId: Int,
    var id: Int,
    val queue: Int,
    val repetitionsCount: Int?,
    val subWorkoutId: Int,
    val approachesCount: Int?,
    val time: Int?,
    var titleRu: String,
    var titleEn: String,
)
