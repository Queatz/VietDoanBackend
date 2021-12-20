package social.hoc

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import social.hoc.plugins.*

fun main() {
    embeddedServer(CIO, port = 80, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureHTTP()
    }.start(wait = true)
}
