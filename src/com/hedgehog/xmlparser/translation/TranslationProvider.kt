package com.hedgehog.xmlparser.translation

import com.hedgehog.xmlparser.translation.platforms.android.ExcelToXml
import com.hedgehog.xmlparser.translation.platforms.android.XmlToExcel
import com.hedgehog.xmlparser.translation.platforms.ios.ExcelToStrings
import com.hedgehog.xmlparser.translation.platforms.ios.StringsToExcel
import java.io.File

object TranslationProvider {

    fun androidXmlToExcel(file: File): File {
        return XmlToExcel.convertToExcel(file)
    }

    fun androidExcelToXml(file: File): File {
        return ExcelToXml.convertToXml(file)
    }

    fun iosStringsToExcel(receivedFile: File): File {
        return StringsToExcel.convertToExcel(receivedFile)
    }

    fun iosExcelToStrings(receivedFile: File): File {
        return ExcelToStrings.convertToStrings(receivedFile)
    }

}