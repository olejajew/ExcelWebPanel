package com.hdghg.translation.data

enum class TaskType(val id: Int) {
    stringsToExcel(0),
    excelToStrings(1);

    companion object{
        fun getById(id: Int): TaskType {
            return values().firstOrNull {
                it.id == id
            } ?: stringsToExcel
        }
    }
}