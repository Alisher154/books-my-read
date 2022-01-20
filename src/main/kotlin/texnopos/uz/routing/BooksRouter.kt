package texnopos.uz.routing


import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import texnopos.uz.entities.BookEntity
import texnopos.uz.entities.UserEntity
import texnopos.uz.models.Book
import texnopos.uz.models.BookRequest
import texnopos.uz.models.GenericResponse
import texnopos.uz.models.UserResponse


fun Route.bookRoutes(db: Database) {

    route("books") {

        post {
            val request = call.receive<BookRequest>()
            if (!request.posterId.isExistUser(db)) {
                call.respond(
                    HttpStatusCode.BadRequest, GenericResponse(
                        success = false,
                        message = "User is not exist",
                        data = ""
                    )
                )
                return@post
            }
            if (request.bookName.isExistBook(db)) {
                call.respond(
                    HttpStatusCode.BadRequest, GenericResponse(
                        success = false,
                        message = "This book already exist",
                        data = ""
                    )
                )
                return@post
            }

            val result = db.insert(BookEntity) {
                set(it.bookName, request.bookName)
                set(it.author, request.author)
                set(it.posterId, request.posterId)
                set(it.createdAt, System.currentTimeMillis())
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
                val posterId = it[BookEntity.posterId]
                Book(id ?: -1, book ?: "", author ?: "", posterId ?: -1)
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
            val book = db.from(BookEntity).select()
                .where { BookEntity.id eq id }
                .map {
                    val _id = it[BookEntity.id]!!
                    val bookName = it[BookEntity.bookName]!!
                    val author = it[BookEntity.author]!!
                    val posterId = it[BookEntity.posterId]!!
                    Book(_id, bookName, author, posterId)
                }.firstOrNull()
            if (book != null) {
                call.respond(
                    HttpStatusCode.OK,
                    GenericResponse(
                        success = true,
                        message = "Successfully",
                        data = book
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

fun Int?.isExistUser(db: Database): Boolean {
    return db.from(UserEntity).select().map { it[UserEntity.id] }.contains(this)
}

fun String.isExistBook(db: Database): Boolean {
    return db.from(BookEntity).select().map { it[BookEntity.bookName] ?: "".lowercase() }.contains(this.lowercase())
}

fun Int?.isExistBook(db: Database): Boolean {
    return db.from(BookEntity).select().map { it[BookEntity.id] }.contains(this)
}
