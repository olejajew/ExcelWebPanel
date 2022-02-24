package com.hedgehog.xmlparser.translation.data

enum class PlatformType(val id: Int) {
    android(0),
    ios(1);

    companion object {
        fun getPlatformById(id: Int): PlatformType {
            return values().firstOrNull { it.id == id } ?: android
        }

    }
}