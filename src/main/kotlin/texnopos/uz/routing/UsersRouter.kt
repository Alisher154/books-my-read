package texnopos.uz.routing

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import texnopos.uz.entities.UserEntity
import texnopos.uz.models.GenericResponse
import texnopos.uz.models.UserResponse

fun Route.userRoutes(db: Database) {

    route("users") {
        get {
            val users = db.from(UserEntity).select().map {
                val id = it[UserEntity.id]
                val username = it[UserEntity.username]
                val name = it[UserEntity.name]
                val surname = it[UserEntity.surname]
                UserResponse(id, username, name, surname)
            }
            if (users.isEmpty()) {
                call.respond(
                    HttpStatusCode.NotFound, GenericResponse(
                        success = false,
                        message = "Users not found",
                        data = users
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK, GenericResponse(
                        success = true,
                        message = "${users.size} user found",
                        data = users
                    )
                )
            }
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val user = db.from(UserEntity).select()
                .where { UserEntity.id eq id }
                .map {
                    val mId = it[UserEntity.id]
                    val username = it[UserEntity.username]
                    val name = it[UserEntity.name]
                    val surname = it[UserEntity.surname]
                    UserResponse(mId, username, name, surname)
                }.firstOrNull()
            if (user != null) {
                call.respond(
                    HttpStatusCode.OK, GenericResponse(
                        success = true,
                        message = "User found",
                        data = user
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, GenericResponse(
                        success = false,
                        message = "User not found",
                        data = ""
                    )
                )
            }
        }

    }
}