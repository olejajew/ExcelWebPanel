package com.hedgehog.xmlparser.translation.platforms.android

import com.hedgehog.xmlparser.utils.replacePlaceholder
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object ExcelToXml {
    private const val RESULT_PATH = "./files/translation/result"

    fun convertToXml(file: File): File {
        clearDirectory()
        val resultFolder = File(RESULT_PATH)
        if (resultFolder.exists()) {
            resultFolder.delete()
        }
        resultFolder.mkdirs()

        val wb = XSSFWorkbook(FileInputStream(file))
        val sheet = wb.getSheetAt(0)

        val languages = getLocalesMap(sheet.getRow(0))
        val locales = getLocales(sheet, languages)
        val arrays = getArrays(sheet, languages)

        var fileName = ""

        for (i in 0 until languages.size) {
            fileName = saveToXml(languages[i], locales[i], arrays.get(languages[i])!!)
        }

        return putIntoZip(fileName)
    }

    private fun getLocales(sheet: XSSFSheet, languages: ArrayList<String>): ArrayList<HashMap<String, String>> {
        val locales = arrayListOf<HashMap<String, String>>()
        for (i in 0 until languages.size) {
            locales.add(HashMap())
        }

        var index = 1
        var row = sheet.getRow(index++)
        var newPart = true
        while (row?.getCell(0) != null) {
            if (row.getCell(0).cellType == Cell.CELL_TYPE_NUMERIC) {
                newPart = true
            } else if (newPart) {
                val tag = row.getCell(0).stringCellValue
                if (tag.trim().isNotEmpty() && !tag.startsWith("0")) {
                    for (i in 0 until languages.size) {
                        if (row.getCell(1 + i) != null) {
                            val value = try {
                                row.getCell(1 + i).stringCellValue.trim()
                            } catch (e: Exception) {
                                row.getCell(1 + i).numericCellValue.toString().trim()
                            }
                            if (value.isNotEmpty()) {
                                locales[i][tag] = value
                            }
                        }
                    }
                }
            }
            row = sheet.getRow(index++)
        }
        return locales
    }

    private fun getArrays(
        sheet: XSSFSheet,
        languages: ArrayList<String>
    ): HashMap<String, HashMap<String, MutableList<String>>> {
        val map = hashMapOf<String, HashMap<String, MutableList<String>>>()
        languages.forEach {
            map[it] = hashMapOf()
        }

        var index = 1
        var row = sheet.getRow(index++)
        var newPart = true
        while (row?.getCell(0) != null) {
            if (row.getCell(0).cellType == Cell.CELL_TYPE_NUMERIC) {
                newPart = true
            } else if (newPart) {
                val tag = row.getCell(0).stringCellValue
                if (tag.trim().isNotEmpty() && tag.startsWith("0")) {
                    val arrayName = tag.split("_")[1]
                    for (i in 0 until languages.size) {
                        if (row.getCell(1 + i) != null) {
                            val value = try {
                                row.getCell(1 + i).stringCellValue.trim()
                            } catch (e: Exception) {
                                row.getCell(1 + i).numericCellValue.toString().trim()
                            }
                            if (value.isNotEmpty()) {
                                if (!map[languages[i]]!!.containsKey(arrayName)) {
                                    map[languages[i]]!![arrayName] = mutableListOf()
                                }
                                map[languages[i]]!![arrayName]!!.add(value)
                            }
                        }
                    }
                }
            }
            row = sheet.getRow(index++)
        }
        return map
    }

    private fun saveToXml(
        language: String,
        data: HashMap<String, String>,
        arrays: java.util.HashMap<String, MutableList<String>>,
    ): String {
        val document = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .newDocument()

        val fileName = if (data.isNotEmpty()) {
            "strings"
        } else {
            "arrays"
        }

        val root = document.createElement("resources")
        document.appendChild(root)

        data.forEach { (key, value) ->
            val element = document.createElement("string")
            element.setAttribute("name", key)
            val value = if (key.contains("separator")) {
                "======"
            } else {
                value
            }
            println(value)
            element.textContent = value
            root.appendChild(element)
        }
        arrays.forEach { key, list ->
            val element = document.createElement("array")
            element.setAttribute("name", key)
            list.forEach {
                val child = document.createElement("item")
                child.textContent = it
                element.appendChild(child)
            }
            root.appendChild(element)
        }

        val transformer = TransformerFactory.newInstance().newTransformer()
        val source = DOMSource(document)
        val result = StreamResult("$RESULT_PATH/$fileName-$language.xml")
        transformer.transform(source, result)
        return fileName
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
        File(RESULT_PATH).deleteRecursively()
    }

    private fun putIntoZip(fileName: String): File {
        File("$RESULT_PATH/result.zip").delete()
        ZipOutputStream(BufferedOutputStream(FileOutputStream("$RESULT_PATH/result.zip"))).use { out ->
            for (file in File(RESULT_PATH).listFiles().filter { !it.extension.contains("zip") }) {
                println(file.readText())
                file.writeText(file.readText().replacePlaceholder())
                println(file.readText())
                FileInputStream(file).use { fi ->
                    BufferedInputStream(fi).use { origin ->
                        val language = file.nameWithoutExtension.split("-")[1]
                        val folderName = "values-$language/"
                        val folder = ZipEntry(folderName)
                        val entry = ZipEntry("${folderName}${fileName}.xml")
                        out.putNextEntry(folder)
                        out.putNextEntry(entry)
                        origin.copyTo(out, 1024)
                    }
                }
            }
        }
        return File("$RESULT_PATH/result.zip")
    }
}