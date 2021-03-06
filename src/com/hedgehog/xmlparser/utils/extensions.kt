package com.hedgehog.xmlparser.utils

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

suspend fun ApplicationCall.badRequest() {
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
    .replace("'", "\\'", true)
    .replace("&lt;", "<", true)
    .replace("&gt;", ">", true)
    .replace("»", "\"")
    .replace("«", "\"")
    .replace("„", "\"")
    .replace("“", "\"")
    .replace("「", "\"")
    .replace("」", "\"")
    .replace("1002916", "%.4f", true)
    .replace("1002815", " %.4f", true)
    .replace("1002714", "%.4f ", true)
    .replace("1002613", " %.4f ", true)
    .replace("1002512", "%.3f", true)
    .replace("1002411", " %.3f", true)
    .replace("1002310", "%.3f ", true)
    .replace("100229", " %.3f ", true)
    .replace("100218", "%.2f", true)
    .replace("100207", " %.2f", true)
    .replace("100196", "%.2f ", true)
    .replace("100185", " %.2f ", true)
    .replace("100174", "%.1f", true)
    .replace("100163", " %.1f", true)
    .replace("100152", "%.1f ", true)
    .replace("100141", " %.1f ", true)
    .replace("10098", "<a href=''>", true)
    .replace("10097", "]]>", true)
    .replace("10096", "<![CDATA[", true)
    .replace("10095", "</br>", true)
    .replace("10094", "<br>", true)
    .replace("10093", "</img>", true)
    .replace("10092", "<img>", true)
    .replace("10091", "</h6>", true)
    .replace("10090", "<h6>", true)
    .replace("10089", "</h5>", true)
    .replace("10088", "<h5>", true)
    .replace("10087", "</h4>", true)
    .replace("10086", "<h4>", true)
    .replace("10085", "</h3>", true)
    .replace("10084", "<h3>", true)
    .replace("10083", "</h2>", true)
    .replace("10082", "<h2>", true)
    .replace("10081", "</h1>", true)
    .replace("10080", "<h1>", true)
    .replace("10079", "</sub>", true)
    .replace("10078", "<sub>", true)
    .replace("10077", "</sup>", true)
    .replace("10076", "<sup>", true)
    .replace("10075", "</strike>", true)
    .replace("10074", "<strike>", true)
    .replace("10073", "</s>", true)
    .replace("10072", "<s>", true)
    .replace("10071", "</del>", true)
    .replace("10070", "<del>", true)
    .replace("10069", "</u>", true)
    .replace("10068", "<u>", true)
    .replace("10067", "</a>", true)
    .replace("10066", "<a>", true)
    .replace("10065", "</tt>", true)
    .replace("10064", "<tt>", true)
    .replace("10063", "</blockquote>", true)
    .replace("10062", "<blockquote>", true)
    .replace("10061", "</font>", true)
    .replace("10060", "<font>", true)
    .replace("10059", "</small>", true)
    .replace("10058", "<small>", true)
    .replace("10057", "</big>", true)
    .replace("10056", "<big>", true)
    .replace("10055", "</i>", true)
    .replace("10054", "<i>", true)
    .replace("10053", "</dfn>", true)
    .replace("10052", "<dfn>", true)
    .replace("10051", "</cite>", true)
    .replace("10050", "<cite>", true)
    .replace("10049", "</em>", true)
    .replace("10048", "<em>", true)
    .replace("10047", "</b>", true)
    .replace("10046", "<b>", true)
    .replace("10045", "</strong>", true)
    .replace("10044", "<strong>", true)
    .replace("10043", "</span>", true)
    .replace("10042", "<span>", true)
    .replace("10041", "</div>", true)
    .replace("10040", "<div>", true)
    .replace("10039", "</li>", true)
    .replace("10038", "<li>", true)
    .replace("10037", "</ul>", true)
    .replace("10036", "<ul>", true)
    .replace("10035", "</p>", true)
    .replace("10034", "<p>", true)
    .replace("10033", "%d", true)
    .replace("10032", " %d", true)
    .replace("10031", "%d ", true)
    .replace("10030", " %d ", true)
    .replace("10029", "%4\$d", true)
    .replace("10028", " %4\$d", true)
    .replace("10027", "%4\$d ", true)
    .replace("10026", " %4\$d ", true)
    .replace("10025", "%3\$d", true)
    .replace("10024", " %3\$d", true)
    .replace("10023", "%3\$d ", true)
    .replace("10022", " %3\$d ", true)
    .replace("10021", "%2\$d", true)
    .replace("10020", " %2\$d", true)
    .replace("10019", "%2\$d ", true)
    .replace("10018", " %2\$d ", true)
    .replace("10017", "%1\$d", true)
    .replace("10016", " %1\$d", true)
    .replace("10015", "%1\$d ", true)
    .replace("10014", " %1\$d ", true)
    .replace("10013", "%s", true)
    .replace("10012", " %s", true)
    .replace("10011", "%s ", true)
    .replace("10010", " %s ", true)
    .replace("1009", "%2\$s", true)
    .replace("1008", " %2\$s", true)
    .replace("1007", "%2\$s ", true)
    .replace("1006", " %2\$s ", true)
    .replace("1005", "%1\$s", true)
    .replace("1004", " %1\$s", true)
    .replace("1003", "%1\$s ", true)
    .replace("1002", " %1\$s ", true)
    .replace("1001", "\\n", true)

fun String.setPlaceholders() = this
    .replace("%.4f", "1002916", true)
    .replace(" %.4f", "1002815", true)
    .replace("%.4f ", "1002714", true)
    .replace(" %.4f ", "1002613", true)
    .replace("%.3f", "1002512", true)
    .replace(" %.3f", "1002411", true)
    .replace("%.3f ", "1002310", true)
    .replace(" %.3f ", "100229", true)
    .replace("%.2f", "100218", true)
    .replace(" %.2f", "100207", true)
    .replace("%.2f ", "100196", true)
    .replace(" %.2f ", "100185", true)
    .replace("%.1f", "100174", true)
    .replace(" %.1f", "100163", true)
    .replace("%.1f ", "100152", true)
    .replace(" %.1f ", "100141", true)
    .replace("<a href=''>", "10098", true)
    .replace("]]>", "10097", true)
    .replace("<![CDATA[", "10096", true)
    .replace("</br>", "10095", true)
    .replace("<br>", "10094", true)
    .replace("</img>", "10093", true)
    .replace("<img>", "10092", true)
    .replace("</h6>", "10091", true)
    .replace("<h6>", "10090", true)
    .replace("</h5>", "10089", true)
    .replace("<h5>", "10088", true)
    .replace("</h4>", "10087", true)
    .replace("<h4>", "10086", true)
    .replace("</h3>", "10085", true)
    .replace("<h3>", "10084", true)
    .replace("</h2>", "10083", true)
    .replace("<h2>", "10082", true)
    .replace("</h1>", "10081", true)
    .replace("<h1>", "10080", true)
    .replace("</sub>", "10079", true)
    .replace("<sub>", "10078", true)
    .replace("</sup>", "10077", true)
    .replace("<sup>", "10076", true)
    .replace("</strike>", "10075", true)
    .replace("<strike>", "10074", true)
    .replace("</s>", "10073", true)
    .replace("<s>", "10072", true)
    .replace("</del>", "10071", true)
    .replace("<del>", "10070", true)
    .replace("</u>", "10069", true)
    .replace("<u>", "10068", true)
    .replace("</a>", "10067", true)
    .replace("<a>", "10066", true)
    .replace("</tt>", "10065", true)
    .replace("<tt>", "10064", true)
    .replace("</blockquote>", "10063", true)
    .replace("<blockquote>", "10062", true)
    .replace("</font>", "10061", true)
    .replace("<font>", "10060", true)
    .replace("</small>", "10059", true)
    .replace("<small>", "10058", true)
    .replace("</big>", "10057", true)
    .replace("<big>", "10056", true)
    .replace("</i>", "10055", true)
    .replace("<i>", "10054", true)
    .replace("</dfn>", "10053", true)
    .replace("<dfn>", "10052", true)
    .replace("</cite>", "10051", true)
    .replace("<cite>", "10050", true)
    .replace("</em>", "10049", true)
    .replace("<em>", "10048", true)
    .replace("</b>", "10047", true)
    .replace("<b>", "10046", true)
    .replace("</strong>", "10045", true)
    .replace("<strong>", "10044", true)
    .replace("</span>", "10043", true)
    .replace("<span>", "10042", true)
    .replace("</div>", "10041", true)
    .replace("<div>", "10040", true)
    .replace("</li>", "10039", true)
    .replace("<li>", "10038", true)
    .replace("</ul>", "10037", true)
    .replace("<ul>", "10036", true)
    .replace("</p>", "10035", true)
    .replace("<p>", "10034", true)
    .replace("%d", "10033", true)
    .replace(" %d", "10032", true)
    .replace("%d ", "10031", true)
    .replace(" %d ", "10030", true)
    .replace("%4\$d", "10029", true)
    .replace(" %4\$d", "10028", true)
    .replace("%4\$d ", "10027", true)
    .replace(" %4\$d ", "10026", true)
    .replace("%3\$d", "10025", true)
    .replace(" %3\$d", "10024", true)
    .replace("%3\$d ", "10023", true)
    .replace(" %3\$d ", "10022", true)
    .replace("%2\$d", "10021", true)
    .replace(" %2\$d", "10020", true)
    .replace("%2\$d ", "10019", true)
    .replace(" %2\$d ", "10018", true)
    .replace("%1\$d", "10017", true)
    .replace(" %1\$d", "10016", true)
    .replace("%1\$d ", "10015", true)
    .replace(" %1\$d ", "10014", true)
    .replace("%s", "10013", true)
    .replace(" %s", "10012", true)
    .replace("%s ", "10011", true)
    .replace(" %s ", "10010", true)
    .replace("%2\$s", "1009", true)
    .replace(" %2\$s", "1008", true)
    .replace("%2\$s ", "1007", true)
    .replace(" %2\$s ", "1006", true)
    .replace("%1\$s", "1005", true)
    .replace(" %1\$s", "1004", true)
    .replace("%1\$s ", "1003", true)
    .replace(" %1\$s ", "1002", true)
    .replace("\\n", "1001", true)


fun Element.isBanned(): Boolean {
    return hasAttribute("translatable") || getTextContent().contains("ca-app")
}