package texnopos.uz.routing

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.forEach
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import texnopos.uz.entities.UserEntity
import texnopos.uz.models.GenericResponse
import texnopos.uz.models.UserResponse

fun Route.userRoutes(db: Database){

    route("users"){
        get{
            val users=db.from(UserEntity).select().map {
                val id=it[UserEntity.id]
                val username=it[UserEntity.username]
                val name=it[UserEntity.name]
                val surname=it[UserEntity.surname]
                UserResponse(id,username,name,surname)
            }
            if (users.isEmpty()){
                call.respond(
                    HttpStatusCode.NotFound,GenericResponse(
                    success = false,
                    message = "Users not found",
                    data = users
                ))
            }else{
                call.respond(
                    HttpStatusCode.OK, GenericResponse(
                    success = true,
                    message = "${users.size} user found",
                    data = users
                ))
            }
        }
    }
}