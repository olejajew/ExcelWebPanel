package com.hedgehog.xmlparser

import com.hdghg.com.hedgehog.xmlparser.RoutingProvider
import com.hedgehog.xmlparser.test.TestFirst
import com.hedgehog.xmlparser.test.TestSecond
import com.hedgehog.xmlparser.test.TestThird
import com.hedgehog.xmlparser.translation.platforms.android.ExcelToXml
import com.hedgehog.xmlparser.translation.platforms.android.XmlToExcel
import com.hedgehog.xmlparser.web.webPage
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

//            val file = File("./xmlStrings.xlsx")
//            ExcelToXml.convertToXml(file)

//            val file = File("./arrays.xml")
//            XmlToExcel.convertToExcel(file)

//            val stringBuilder = StringBuilder()
//            val file = File("./Placeholders.txt").readText()
//            var index = 1
//            file.lines().forEach {
//                stringBuilder.append(it.replace("100", "100${index++}"))
//                stringBuilder.append("\n")
//            }
//            val resultFile = File("./result.txt")
//            resultFile.writeText(stringBuilder.toString())

//            val file = XmlToExcel.convertToExcel(File("./strings.xml"))
//            val file = ExcelToXml.convertToXml(File("./test.xlsx"))
//            println(file)

            RoutingProvider.init()
            val server = embeddedServer(Netty, port = System.getenv()?.get("PORT")?.toIntOrNull() ?: 19252) {
                install(CORS) {
                    method(HttpMethod.Get)
                    method(HttpMethod.Delete)
                    method(HttpMethod.Post)
                    anyHost()
                }

                routing {
                    route("translation") {
                        post("file") {
                            RoutingProvider.parseFile(call)
                        }
                        post("test") {
                            call.respond("Ok")
                        }
                    }
                    webPage()
                }

            }
            server.start(true)
        }
    }
}
