package texnopos.uz.models

import kotlinx.serialization.Serializable

@Serializable
data class ReadResponse(
    val id: Int?,
    val readerName:String?,
    val readerSurname: String?,
    val bookId: Int?,
    val conclusion: String?,
    val createdAt: Long?,
    val updatedAt: Long?
)