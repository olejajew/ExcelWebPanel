package com.hedgehog.xmlparser

import com.hdghg.com.hedgehog.xmlparser.RoutingProvider
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.hedgehog.xmlparser.web.webPage

class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
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
