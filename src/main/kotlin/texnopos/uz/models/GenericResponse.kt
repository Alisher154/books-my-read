package texnopos.uz.models

import kotlinx.serialization.Serializable


@Serializable
data class GenericResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)
