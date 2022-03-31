package com.hedgehog.xmlparser.test

data class SecondModel(
    val exerciseId: Int,
    var id: Int,
    val queue: Int,
    val repetitionsCount: Int?,
    val subWorkoutId: Int,
    val approachesCount: Int?,
    val time: Int?,
    var title: String
)