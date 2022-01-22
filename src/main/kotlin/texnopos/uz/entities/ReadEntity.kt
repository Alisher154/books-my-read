package texnopos.uz.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object ReadEntity:Table<Nothing>("books_read") {
    val id=int("id").primaryKey()
    val readerId=int("readerId")
    val bookId=int("bookId")
    val conclusion=varchar("conclusion")
    val createdAt=long("createdAt")
    val updatedAt=long("updatedAt")
}