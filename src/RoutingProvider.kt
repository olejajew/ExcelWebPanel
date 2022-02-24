package com.hdghg

import com.hdghg.translation.TranslationProvider
import com.hdghg.translation.data.PlatformType
import com.hdghg.translation.data.TaskType
import com.hdghg.utils.FileProvider
import com.hdghg.utils.badRequest
import io.ktor.application.*
import io.ktor.response.*
import java.lang.Exception

object RoutingProvider {

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