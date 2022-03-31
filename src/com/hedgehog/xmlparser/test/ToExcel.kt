package com.hedgehog.xmlparser.test

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

object ToExcel {

    private const val RESULT_FILE_PATH = "./result.xlsx"

    init {
        val resultFile = File(RESULT_FILE_PATH)
        resultFile.createNewFile()
    }

    fun saveToExcel(list: List<ThirdModel>) {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Strings")
        var index = 0
        list.forEach {
            val row = sheet.createRow(index++)
            row.createCell(0).setCellValue(it.id.toString())
            row.createCell(1).setCellValue(it.title)
        }

        val fileOut = FileOutputStream(RESULT_FILE_PATH)
        wb.write(fileOut)
        fileOut.close()
    }


}