package texnopos.uz.routing

import texnopos.uz.db.DatabaseConnection
import texnopos.uz.entities.UserEntity
import texnopos.uz.models.GenericResponse
import texnopos.uz.models.User
import texnopos.uz.models.UserCredentials
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database

import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt
import texnopos.uz.utils.TokenManager


fun Application.authenticationRoutes(db:Database) {
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    routing {

        post("/register") {
            val userCredentials = call.receive<UserCredentials>()

            if (!userCredentials.isValidCredentials()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(
                        success = false,
                        message = "Username greater than 2 and password greater than 6",
                        data = ""
                    )
                )
                return@post
            }
            val username = userCredentials.username.lowercase()
            val password = userCredentials.hashedPassword()

            //User validation
            val user = db.from(UserEntity)
                .select()
                .where { UserEntity.username eq username }
                .map { it[UserEntity.username] }
                .firstOrNull()
            if (user != null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(
                        success = false,
                        message = "User already exists, please try a different username",
                        data = ""
                    )
                )
                return@post
            }

            db.insert(UserEntity) {
                set(it.username, username)
                set(it.password, password)
                set(it.name, "default")
                set(it.surname, "default")
            }

            call.respond(
                HttpStatusCode.Created,
                GenericResponse(
                    success = true,
                    message = "User successfully created",
                    data = ""
                )
            )
        }

        post("/login") {

            val userCredentials = call.receive<UserCredentials>()

            if (!userCredentials.isValidCredentials()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(
                        success = false,
                        message = "Username greater than 2 and password greater than 6",
                        data = ""
                    )
                )
                return@post
            }
            val username = userCredentials.username.lowercase()
            val password = userCredentials.password
            //Check user exists
            val user = db.from(UserEntity)
                .select()
                .where { UserEntity.username eq username }
                .map {
                    val id = it[UserEntity.id]!!
                    val _username = it[UserEntity.username]!!
                    val _password = it[UserEntity.password]!!
                    val _name = it[UserEntity.password]!!
                    val _surname = it[UserEntity.password]!!
                    User(id, _username, _password, _name, _surname)
                }
                .firstOrNull()
            if (user == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(
                        success = false,
                        message = "Invalid username or password.",
                        data = ""
                    )
                )
                return@post
            }
            val doesPasswordMatch = BCrypt.checkpw(password, user.password)
            if (!doesPasswordMatch) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(
                        success = false,
                        message = "Invalid username or password.",
                        data = ""
                    )
                )
                return@post
            }
            val token = tokenManager.generateJWT(user)
            call.respond(
                HttpStatusCode.OK,
                GenericResponse(
                    success = true,
                    message = "User successfully logged in",
                    data = token
                )
            )
        }
        authenticate {
            get("/me") {
                val principal = call.principal<JWTPrincipal>()!!
                val username = principal.payload.getClaim("username").asString()
                val userId = principal.payload.getClaim("userId").asInt()
                call.respondText("Hello $username with id: $userId")
            }
        }
    }
}