package web

import io.ktor.http.content.*
import io.ktor.routing.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

fun Route.webPage() {
    static {
        staticBasePackage = "static"
        defaultResource("index.html")
        resource("favicon.ico")
        resource("css/app.b4d17bb6.css")
        resource("css/chunk-vendors.64d43e69.css")
        resource("js/app.5e7bc29b.js")
        resource("js/app.5e7bc29b.js.map")
        resource("js/chunk-vendors.278374b0.js")
        resource("js/chunk-vendors.278374b0.js.map")
    }
}