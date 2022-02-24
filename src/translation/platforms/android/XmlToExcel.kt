package com.hdghg.translation.platforms.android

import com.hdghg.utils.isBanned
import com.hdghg.utils.setPlaceholders
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import java.io.FileOutputStream
import javax.xml.parsers.DocumentBuilderFactory

object XmlToExcel {

    private const val RESULT_FILE_PATH = "./files/translation/xmlStrings.xlsx"

    fun convertToExcel(file: File): File {
        val map = parseXmlFile(file)
        saveToExcel(map)
        return File(RESULT_FILE_PATH)
    }

    private fun saveToExcel(map: HashMap<String, String>) {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Strings")
        sheet.createRow(0).createCell(1).setCellValue("en")

        var index = 1
        map.forEach { t, u ->
            val row = sheet.createRow(index++)
            row.createCell(0).setCellValue(t)
            row.createCell(1).setCellValue(u)
        }

        val fileOut = FileOutputStream(RESULT_FILE_PATH)
        wb.write(fileOut)
        fileOut.close()
    }

    private fun parseXmlFile(file: File): HashMap<String, String> {
        val map = hashMapOf<String, String>()

        val document = getXmlDocument(file)

        val strings = document.getElementsByTagName("string")
        for (i in 0 until strings.length) {
            val element = strings.item(i) as Element
            if (!element.isBanned()) {
                map[element.getAttribute("name")] = element.textContent.setPlaceholders()
            }
        }
        return map
    }

    private fun getXmlDocument(file: File): Document {
        file.writeText(file.readText().replace("&", "&#038;"))
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        return builder.parse(file)
    }
}