package com.hdghg.translation.platforms.android

import com.hdghg.utils.replacePlaceholder
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFRow
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
        File(RESULT_PATH).mkdirs()
        val wb = XSSFWorkbook(FileInputStream(file))
        val sheet = wb.getSheetAt(0)

        val languages = getLocalesMap(sheet.getRow(0))
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
                if (tag.trim().isNotEmpty()) {
                    println(tag)
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

        for (i in 0 until languages.size) {
            println("HERE!!!")
            saveToXml(languages[i], locales[i])
        }

        return putIntoZip()
    }

    private fun saveToXml(language: String, data: HashMap<String, String>) {
        val document = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .newDocument()

        val root = document.createElement("resources")
        document.appendChild(root)

        data.forEach { (key, value) ->
            val element = document.createElement("string")
            element.setAttribute("name", key)
            val value = if (key.contains("separator")) {
                "======"
            } else {
                value.replacePlaceholder()
            }
            element.textContent = value
            root.appendChild(element)
        }

        val transformer = TransformerFactory.newInstance().newTransformer()
        val source = DOMSource(document)
        val result = StreamResult("$RESULT_PATH/strings-$language.xml")
        transformer.transform(source, result)
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

    private fun putIntoZip(): File {
        File("$RESULT_PATH/result.zip").delete()
        ZipOutputStream(BufferedOutputStream(FileOutputStream("$RESULT_PATH/result.zip"))).use { out ->
            for (file in File(RESULT_PATH).listFiles().filter { !it.extension.contains("zip") }) {
                FileInputStream(file).use { fi ->
                    BufferedInputStream(fi).use { origin ->
                        val entry = ZipEntry(file.name)
                        out.putNextEntry(entry)
                        origin.copyTo(out, 1024)
                    }
                }
            }
        }
        return File("$RESULT_PATH/result.zip")
    }
}