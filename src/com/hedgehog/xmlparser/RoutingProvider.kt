package com.hdghg.com.hedgehog.xmlparser

import com.hedgehog.xmlparser.translation.TranslationProvider
import com.hedgehog.xmlparser.translation.data.PlatformType
import com.hedgehog.xmlparser.translation.data.TaskType
import com.hedgehog.xmlparser.utils.FileProvider
import com.hedgehog.xmlparser.utils.badRequest
import io.ktor.application.*
import io.ktor.response.*
import java.io.File
import java.lang.Exception

object RoutingProvider {

    fun init() {
        val taskId = 1
        val platformType = PlatformType.getPlatformById(0)
        val receivedFile = File("test.xlsx")
        try {
            val file = if (platformType == PlatformType.android) {
                when (TaskType.getById(taskId)) {
                    TaskType.stringsToExcel -> TranslationProvider.androidXmlToExcel(receivedFile)
                    TaskType.excelToStrings -> TranslationProvider.androidExcelToXml(receivedFile)
                }
            } else {
                when (TaskType.getById(taskId)) {
                    TaskType.stringsToExcel -> TranslationProvider.iosStringsToExcel(receivedFile)
                    TaskType.excelToStrings -> TranslationProvider.iosExcelToStrings(receivedFile)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun parseFile(call: ApplicationCall) {
        val taskId = call.parameters["taskId"]?.toIntOrNull() ?: 0
        val platformType = PlatformType.getPlatformById(call.parameters["platformId"]?.toIntOrNull() ?: 0)
        val receivedFile = FileProvider.getFile(call)
        if (receivedFile == null) {
            call.badRequest()
            return
        }
        try {
            val file = if (platformType == PlatformType.android) {
                when (TaskType.getById(taskId)) {
                    TaskType.stringsToExcel -> TranslationProvider.androidXmlToExcel(receivedFile)
                    TaskType.excelToStrings -> TranslationProvider.androidExcelToXml(receivedFile)
                }
            } else {
                when (TaskType.getById(taskId)) {
                    TaskType.stringsToExcel -> TranslationProvider.iosStringsToExcel(receivedFile)
                    TaskType.excelToStrings -> TranslationProvider.iosExcelToStrings(receivedFile)
                }
            }

            call.respondFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            call.badRequest()
        }
    }


}