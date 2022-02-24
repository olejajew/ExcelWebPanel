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
    .replace("»", "\"")
    .replace("«", "\"")
    .replace("„", "\"")
    .replace("“", "\"")
    .replace("「", "\"")
    .replace("」", "\"")
    .replace("skip@newrow.com", "\\n", true)
    .replace("skip@string.com", "%s", true)
    .replace("skip@number.com", "%d", true)
    .replace("skip@p.com", "<p>", true)
    .replace("skip@/p.com", "</p>", true)
    .replace("skip@ul.com", "<ul>", true)
    .replace("skip@/ul.com", "</ul>", true)
    .replace("skip@li.com", "<li>", true)
    .replace("skip@/li.com", "</li>", true)
    .replace("skip@div.com", "<div>", true)
    .replace("skip@/div.com", "</div>", true)
    .replace("skip@span.com", "<span>", true)
    .replace("skip@/span.com", "</span>", true)
    .replace("skip@strong.com", "<strong>", true)
    .replace("skip@/strong.com", "</strong>", true)
    .replace("skip@b.com", "<b>", true)
    .replace("skip@/b.com", "</b>", true)
    .replace("skip@em.com", "<em>", true)
    .replace("skip@/em.com", "</em>", true)
    .replace("skip@cite.com", "<cite>", true)
    .replace("skip@/cite.com", "</cite>", true)
    .replace("skip@dfn.com", "<dfn>", true)
    .replace("skip@/dfn.com", "</dfn>", true)
    .replace("skip@i.com", "<i>", true)
    .replace("skip@/i.com", "</i>", true)
    .replace("skip@big.com", "<big>", true)
    .replace("skip@/big.com", "</big>", true)
    .replace("skip@small.com", "<small>", true)
    .replace("skip@/small.com", "</small>", true)
    .replace("skip@font.com", "<font>", true)
    .replace("skip@/font.com", "</font>", true)
    .replace("skip@blockquote.com", "<blockquote>", true)
    .replace("skip@/blockquote.com", "</blockquote>", true)
    .replace("skip@tt.com", "<tt>", true)
    .replace("skip@/tt.com", "</tt>", true)
    .replace("skip@a.com", "<a>", true)
    .replace("skip@/a.com", "</a>", true)
    .replace("skip@u.com", "<u>", true)
    .replace("skip@/u.com", "</u>", true)
    .replace("skip@del.com", "<del>", true)
    .replace("skip@/del.com", "</del>", true)
    .replace("skip@s.com", "<s>", true)
    .replace("skip@/s.com", "</s>", true)
    .replace("skip@strike.com", "<strike>", true)
    .replace("skip@/strike.com", "</strike>", true)
    .replace("skip@sup.com", "<sup>", true)
    .replace("skip@/sup.com", "</sup>", true)
    .replace("skip@sub.com", "<sub>", true)
    .replace("skip@/sub.com", "</sub>", true)
    .replace("skip@h1.com", "<h1>", true)
    .replace("skip@/h1.com", "</h1>", true)
    .replace("skip@h2.com", "<h2>", true)
    .replace("skip@/h2.com", "</h2>", true)
    .replace("skip@h3.com", "<h3>", true)
    .replace("skip@/h3.com", "</h3>", true)
    .replace("skip@h4.com", "<h4>", true)
    .replace("skip@/h4.com", "</h4>", true)
    .replace("skip@h5.com", "<h5>", true)
    .replace("skip@/h5.com", "</h5>", true)
    .replace("skip@h6.com", "<h6>", true)
    .replace("skip@/h6.com", "</h6>", true)
    .replace("skip@img.com", "<img>", true)
    .replace("skip@/img.com", "</img>", true)
    .replace("skip@br.com", "<br>", true)
    .replace("skip@/br.com", "</br>", true)
    .replace("skip@maintag.com", "<![CDATA[", true)
    .replace("skip@/maintag.com", "]]>", true)

fun String.setPlaceholders() = this
    .replace("\\n", "skip@newrow.com", true)
    .replace("%s", "skip@string.com", true)
    .replace("%d", "skip@number.com", true)
    .replace("<p>", "skip@p.com", true)
    .replace("</p>", "skip@/p.com", true)
    .replace("<ul>", "skip@ul.com", true)
    .replace("</ul>", "skip@/ul.com", true)
    .replace("<li>", "skip@li.com", true)
    .replace("</li>", "skip@/li.com", true)
    .replace("<div>", "skip@div.com", true)
    .replace("</div>", "skip@/div.com", true)
    .replace("<span>", "skip@span.com", true)
    .replace("</span>", "skip@/span.com", true)
    .replace("<strong>", "skip@strong.com", true)
    .replace("</strong>", "skip@/strong.com", true)
    .replace("<b>", "skip@b.com", true)
    .replace("</b>", "skip@/b.com", true)
    .replace("<em>", "skip@em.com", true)
    .replace("</em>", "skip@/em.com", true)
    .replace("<cite>", "skip@cite.com", true)
    .replace("</cite>", "skip@/cite.com", true)
    .replace("<dfn>", "skip@dfn.com", true)
    .replace("</dfn>", "skip@/dfn.com", true)
    .replace("<i>", "skip@i.com", true)
    .replace("</i>", "skip@/i.com", true)
    .replace("<big>", "skip@big.com", true)
    .replace("</big>", "skip@/big.com", true)
    .replace("<small>", "skip@small.com", true)
    .replace("</small>", "skip@/small.com", true)
    .replace("<font>", "skip@font.com", true)
    .replace("</font>", "skip@/font.com", true)
    .replace("<blockquote>", "skip@blockquote.com", true)
    .replace("</blockquote>", "skip@/blockquote.com", true)
    .replace("<tt>", "skip@tt.com", true)
    .replace("</tt>", "skip@/tt.com", true)
    .replace("<a>", "skip@a.com", true)
    .replace("</a>", "skip@/a.com", true)
    .replace("<u>", "skip@u.com", true)
    .replace("</u>", "skip@/u.com", true)
    .replace("<del>", "skip@del.com", true)
    .replace("</del>", "skip@/del.com", true)
    .replace("<s>", "skip@s.com", true)
    .replace("</s>", "skip@/s.com", true)
    .replace("<strike>", "skip@strike.com", true)
    .replace("</strike>", "skip@/strike.com", true)
    .replace("<sup>", "skip@sup.com", true)
    .replace("</sup>", "skip@/sup.com", true)
    .replace("<sub>", "skip@sub.com", true)
    .replace("</sub>", "skip@/sub.com", true)
    .replace("<h1>", "skip@h1.com", true)
    .replace("</h1>", "skip@/h1.com", true)
    .replace("<h2>", "skip@h2.com", true)
    .replace("</h2>", "skip@/h2.com", true)
    .replace("<h3>", "skip@h3.com", true)
    .replace("</h3>", "skip@/h3.com", true)
    .replace("<h4>", "skip@h4.com", true)
    .replace("</h4>", "skip@/h4.com", true)
    .replace("<h5>", "skip@h5.com", true)
    .replace("</h5>", "skip@/h5.com", true)
    .replace("<h6>", "skip@h6.com", true)
    .replace("</h6>", "skip@/h6.com", true)
    .replace("<img>", "skip@img.com", true)
    .replace("</img>", "skip@/img.com", true)
    .replace("<br>", "skip@br.com", true)
    .replace("</br>", "skip@/br.com", true)
    .replace("<![CDATA[", "skip@maintag.com", true)
    .replace("]]>", "skip@/maintag.com", true)


fun Element.isBanned(): Boolean {
    return hasAttribute("translatable") || getTextContent().contains("ca-app")
}