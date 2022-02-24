package com.hdghg

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import web.webPage

class Application{
    companion object{
        @JvmStatic
        fun main(args: Array<String>){
            val server = embeddedServer(Netty, port = 19252){
                install(CORS) {
                    method(HttpMethod.Get)
                    method(HttpMethod.Delete)
                    method(HttpMethod.Post)
                    anyHost()
                }

                routing {
                    route("translation"){
                        post("file"){
                            RoutingProvider.parseFile(call)
                        }
                        post("test"){
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