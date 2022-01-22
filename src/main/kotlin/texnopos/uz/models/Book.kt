package texnopos.uz.models

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val bookName: String,
    val author:String,
    val posterFullName:String
)
