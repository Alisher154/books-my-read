package texnopos.uz.models

import kotlinx.serialization.Serializable

@Serializable
data class BookRequest(
    val bookName: String,
    val author: String,
    val posterId:Int
)
