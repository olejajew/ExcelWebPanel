package com.hdghg.utils

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.w3c.dom.Element
import java.io.InputStream
import java.io.OutputStream

suspend fun ApplicationCall.badRequest(){
    respond(HttpStatusCode.BadRequest)
}

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}

fun String.replacePlaceholder() = this
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

fun String.setPlaceholders() = this
    .replace("\\n", " 777 ")
    .replace("%s", " 888 ")
    .replace("%d", " 999 ")

fun Element.isBanned(): Boolean {
    return hasAttribute("translatable") || textContent.contains("ca-app")
}