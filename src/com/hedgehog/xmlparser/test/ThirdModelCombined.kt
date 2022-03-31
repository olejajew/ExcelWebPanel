package com.hedgehog.xmlparser.test

data class ThirdModelCombined(
    val id: Int,
    val workoutId: Int,
    var titleRu: String?,
    var titleEn: String?,
    val time: String,
    val exerciseCount: Int?,
    val completedCount: Int?,
    val hardType: Int,
    val type: Int,
    val difficulty: Int
)