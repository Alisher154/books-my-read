package texnopos.uz.models

import kotlinx.serialization.Serializable

@Serializable
data class ReadResponse(
    val id: Int?,
    val readerFullName:String?,
    val bookName: String?,
    val conclusion: String?,
    val createdAt: Long?,
    val updatedAt: Long?
)