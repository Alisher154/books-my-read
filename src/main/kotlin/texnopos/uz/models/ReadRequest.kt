package texnopos.uz.models

import kotlinx.serialization.Serializable

@Serializable
data class ReadRequest(
    val bookId: Int?,
    val conclusion: String?,
)