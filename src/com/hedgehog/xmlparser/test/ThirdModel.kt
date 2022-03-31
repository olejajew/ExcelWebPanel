package com.hedgehog.xmlparser.test

data class ThirdModel(
    val id: Int,
    val workoutId: Int,
    var title: String?,
    var titleEn: String?,
    var titleRu: String?,
    val time: String,
    val exerciseCount: Int?,
    val completedCount: Int?,
    val hardType: Int,
    val type: Int,
    val difficulty: Int
)