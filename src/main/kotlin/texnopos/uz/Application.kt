package texnopos.uz

import texnopos.uz.plugins.configureAuthentication
import texnopos.uz.plugins.configureRouting
import texnopos.uz.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    //port = System.getenv("PORT").toInt()
    embeddedServer(Netty, port = (System.getenv("PORT")?:"5000").toInt()) {
        configureSerialization()
        configureAuthentication()
        configureRouting()
    }.start(wait = true)
}
