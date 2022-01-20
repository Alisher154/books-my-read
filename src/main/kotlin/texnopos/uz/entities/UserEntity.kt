package texnopos.uz.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.varchar

object UserEntity : Table<Nothing>(tableName = "users") {
    val id = int("id").primaryKey()
    val username = varchar("username")
    val password = varchar("password")
    val name = varchar("name")
    val surname = varchar("surname")
    val createdAt=long("createdAt")
}