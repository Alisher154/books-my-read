package texnopos.uz.routing


import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import texnopos.uz.entities.BookEntity
import texnopos.uz.models.Book
import texnopos.uz.models.BookRequest
import texnopos.uz.models.GenericResponse


fun Route.bookRoutes(db: Database) {
    route("books") {

        post {
            val request = call.receive<BookRequest>()
            val result = db.insert(BookEntity) {
                set(it.bookName, request.bookName)
                set(it.author, request.author)
            }
            if (result == 1) {
                call.respond(
                    HttpStatusCode.OK, GenericResponse(
                        success = true,
                        message = "Book has been successfully inserted",
                        data = ""
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, GenericResponse(
                        success = false,
                        message = "Failed to insert book.",
                        data = ""
                    )
                )
            }
        }

        get {
            val books = db.from(BookEntity).select().map {
                val id = it[BookEntity.id]
                val book = it[BookEntity.bookName]
                val author = it[BookEntity.author]
                Book(id ?: -1, book ?: "", author ?: "")
            }
            if (books.isEmpty()) {
                call.respond(
                    HttpStatusCode.NotFound, GenericResponse(
                        success = false,
                        message = "Books not found",
                        data = books
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(
                        success = true,
                        message = "${books.size} books found",
                        data = books
                    )
                )
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val note = db.from(BookEntity).select()
                .where { BookEntity.id eq id }
                .map {
                    val _id = it[BookEntity.id]!!
                    val note = it[BookEntity.bookName]!!
                    val author = it[BookEntity.author]!!
                    Book(_id, note, author)
                }.firstOrNull()
            if (note != null) {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(
                        success = true,
                        message = "Successfully",
                        data = note
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    GenericResponse(
                        success = false,
                        message = "Could not found book with id = $id",
                        data = ""
                    )
                )
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val updatedNote = call.receive<BookRequest>()
            val rowEffected = db.update(BookEntity) {
                set(it.bookName, updatedNote.bookName)
                set(it.author, updatedNote.author)
                where {
                    it.id eq id
                }
            }
            if (rowEffected == 1) {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(
                        success = true,
                        message = "Book has been updated",
                        data = ""
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.NotModified,
                    GenericResponse(
                        success = false,
                        message = "Book fails to updated",
                        data = ""
                    )
                )
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val rowEffected = db.delete(BookEntity) {
                it.id eq id
            }
            if (rowEffected == 1) {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(
                        success = true,
                        message = "Book has been deleted",
                        data = ""
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    GenericResponse(
                        success = false,
                        message = "Book fails to delete",
                        data = ""
                    )
                )
            }
        }
    }
}