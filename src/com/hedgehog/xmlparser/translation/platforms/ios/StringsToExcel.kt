package com.hedgehog.xmlparser.translation.platforms.ios

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

object StringsToExcel {
    private const val RESULT_FILE = "./files/translation/ios/result/result.xlsx"

    fun convertToExcel(file: File): File {
        val f = File(RESULT_FILE)
        File("./files/translation/ios/result/").mkdirs()
        f.createNewFile()
        val strings = getStringsFromFile(file)
        saveToExcel(strings)
        return f
    }

    private fun getStringsFromFile(file: File): ArrayList<Pair<String, String>> {
        val strings = ArrayList<Pair<String, String>>()
        var lastTag: String? = null
        file.readLines().map { it.trim() }.forEach { string ->
            if (!string.isNullOrEmpty()) {
                if (string[0] == '\"') {
                    val parts = string.split("=")
                    lastTag = parts[0]
                    if (parts.size > 1) {
                        val value = parts[1]
                        strings.add(Pair(lastTag!!, value))
                    }
                } else if (lastTag != null) {
                    val last = strings.last()
                    val newPair = Pair(last.first, last.second + " " + string)
                    strings.remove(last)
                    strings.add(newPair)
                }
            }
        }
        return strings
    }

    private fun saveToExcel(map: ArrayList<Pair<String, String>>) {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Strings")
        var index = 1
        map.forEach {
            val row = sheet.createRow(index++)
            row.createCell(0).setCellValue(it.first)
            row.createCell(1).setCellValue(it.second)
        }

        val fileOut = FileOutputStream(RESULT_FILE)
        wb.write(fileOut)
        fileOut.close()
    }

}