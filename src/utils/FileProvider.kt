package com.hdghg.utils

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import java.io.File

object FileProvider {

    suspend fun getFile(call: ApplicationCall): File? {
        var resultFile: File? = null
        val multipart = call.receiveMultipart()
        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    val ext = File(part.originalFileName).extension
                    val file = File("./result", "result.$ext")
                    File("./result").mkdirs()
                    file.createNewFile()
                    part.streamProvider().use { input ->
                        val outs = file.outputStream()
                        val buffered = outs.buffered()
                        buffered.use { output ->
                            input.copyToSuspend(output)
                        }
                    }
                    resultFile = file
                }
            }

            part.dispose()
        }
        return resultFile
    }

}