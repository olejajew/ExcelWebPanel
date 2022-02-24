package com.hdghg.translation.platforms.ios

import com.hdghg.translation.platforms.android.ExcelToXml
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ExcelToStrings {
    private const val RESULT_FILE = "./files/translation/iosresult"

    fun convertToStrings(file: File): File {
        clearDirectory()
        val wb = XSSFWorkbook(FileInputStream(file))
        val sheet = wb.getSheetAt(0)

        val languages = getLocalesMap(sheet.getRow(0))
        val locales = arrayListOf<ArrayList<Pair<String, String>>>()

        for (i in 0 until languages.size) {
            locales.add(ArrayList())
        }

        var index = 1
        var row = sheet.getRow(index++)
        var newPart = true
        while (row?.getCell(0) != null) {
            if (row.getCell(0).cellType == Cell.CELL_TYPE_NUMERIC) {
                newPart = true
            } else if (newPart) {
                val tag = row.getCell(0).stringCellValue
                if (tag.trim().isNotEmpty()) {
                    for (i in 0 until languages.size) {
                        if (row.getCell(1 + i) != null) {
                            val value = row.getCell(1 + i).stringCellValue.trim()
                            var string = row.getCell(1).stringCellValue.trim()

                            if (value.isNotEmpty()) {
                                string = value.replacePlaceholder().trim()
                            }
                            if (!string.startsWith("\"")) {
                                string = "\"$string"
                            }
                            if (!string.endsWith("\";")) {
                                if (!string.endsWith("\"")) {
                                    string = "$string\""
                                }
                                if (!string.endsWith("\";")) {
                                    string = "$string;"
                                }
                            }
                            val sub = string.substring(string.length - 3, string.length)
                            if (!sub.contains("\";")) {
                                println(sub)
                            }
                            if (string.isNotEmpty()) {
                                locales[i].add(Pair(tag, string))
                            }
                        }
                    }
                }
            }
            row = sheet.getRow(index++)
        }

        for (i in 0 until languages.size) {
            saveToIosFile(languages[i], locales[i])
        }

        return putIntoZip()
    }

    private fun putIntoZip(): File {
        ZipOutputStream(BufferedOutputStream(FileOutputStream("$RESULT_FILE/result.zip"))).use { out ->
            for (file in File(RESULT_FILE).listFiles()) {
                FileInputStream(file).use { fi ->
                    BufferedInputStream(fi).use { origin ->
                        val entry = ZipEntry(file.name)
                        out.putNextEntry(entry)
                        origin.copyTo(out, 1024)
                    }
                }
            }
        }
        return File("$RESULT_FILE/result.zip")
    }

    private fun saveToIosFile(s: String, hashMap: ArrayList<Pair<String, String>>) {
        val file = File("$RESULT_FILE/$s.strings.strings")
        File(RESULT_FILE).mkdirs()
        file.createNewFile()
        file.writeText(hashMap.map { "${it.first} = ${it.second}" }.joinToString("\n"))
    }

    private fun getLocalesMap(row: XSSFRow): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        var index = 1
        while (row.getCell(index) != null) {
            val language = row.getCell(index++).stringCellValue
            arrayList.add(language.trim().toLowerCase())
        }
        return arrayList
    }

    private fun clearDirectory() {
        File(RESULT_FILE).deleteRecursively()
    }

    private fun String.replacePlaceholder() = this
        .replace(" 777 ", "\\n")
        .replace(" 777", "\\n")
        .replace("777 ", "\\n")
        .replace(" 888 ", "%s")
        .replace("888 ", "%s")
        .replace(" 888", "%s")
        .replace(" 999 ", "%d")
        .replace(" 999", "%d")
        .replace("999 ", "%d")
        .replace("»", "\"")
        .replace("«", "\"")
        .replace("„", "\"")
        .replace("“", "\"")
        .replace("「", "\"")
        .replace("」", "\"")
}