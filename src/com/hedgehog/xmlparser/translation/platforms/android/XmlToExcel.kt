package com.hedgehog.xmlparser.translation.platforms.android

import com.hedgehog.xmlparser.utils.isBanned
import com.hedgehog.xmlparser.utils.setPlaceholders
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.CharacterData

object XmlToExcel {

    private const val RESULT_FILE_PATH = "./files/translation/xmlStrings.xlsx"

    init {
        val folder = File("./files/translation/")
        folder.mkdirs()
        val resultFile = File(RESULT_FILE_PATH)
        resultFile.createNewFile()
    }

    fun convertToExcel(file: File): File {
        file.writeText(file.readText().setPlaceholders())
        val map = parseXmlFile(file)
        val arrays = parseXmlFileArrays(file)
        saveToExcel(map, arrays)
        val resultFile = File(RESULT_FILE_PATH)
        resultFile.createNewFile()
        return resultFile
    }

    private fun saveToExcel(strings: HashMap<String, String>, arrays: HashMap<String, List<String>>) {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Strings")
        sheet.createRow(0).createCell(1).setCellValue("en")

        var index = 1
        strings.forEach { t, u ->
            val row = sheet.createRow(index++)
            row.createCell(0).setCellValue(t)
            row.createCell(1).setCellValue(u)
        }
        arrays.forEach { t, u ->
            var indexArray = 0
            u.forEach {
                if (it.trim().isNotEmpty()) {
                    val row = sheet.createRow(index++)
                    row.createCell(0).setCellValue("0_${t}_${indexArray++}")
                    row.createCell(1).setCellValue(it)
                }
            }
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
                println((element.childNodes.item(0) as CharacterData).data)
                map[element.getAttribute("name")] = element.getTextContent()
            }
        }

        return map
    }

    private fun parseXmlFileArrays(file: File): HashMap<String, List<String>> {
        val map = hashMapOf<String, List<String>>()
        val document = getXmlDocument(file)

        val arrays = document.getElementsByTagName("array")
        for (i in 0 until arrays.length) {
            val array = arrays.item(i) as Element
            val arrayName = array.getAttribute("name")
            val list = mutableListOf<String>()
            for (i in 0 until array.childNodes.length) {
                val item = array.childNodes.item(i).textContent
                list.add(item)
            }
            map[arrayName] = list
        }

        return map
    }

    private fun getXmlDocument(file: File): Document {
        file.writeText(file.readText().replace("&", "&#038;"))
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        return builder.parse(file)
    }

    fun getCharacterDataFromElement(e: Element): String? {
        val list: NodeList = e.childNodes
        var data: String
        for (index in 0 until list.getLength()) {
            if (list.item(index) is org.w3c.dom.CharacterData) {
                val child = list.item(index) as CharacterData
                data = child.getData()
                if (data != null && data.trim { it <= ' ' }.length > 0) return child.getData()
            }
        }
        return ""
    }
}