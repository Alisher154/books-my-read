package texnopos.uz.plugins

import texnopos.uz.routing.authenticationRoutes
import texnopos.uz.routing.bookRoutes
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import texnopos.uz.db.DatabaseConnection
import texnopos.uz.routing.userRoutes


fun Application.configureRouting() {
    val db = DatabaseConnection.database
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        bookRoutes(db)
        authenticationRoutes(db)
        userRoutes(db)
    }
}
