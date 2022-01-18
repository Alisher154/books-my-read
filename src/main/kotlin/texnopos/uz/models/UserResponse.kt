package texnopos.uz.models

import kotlinx.serialization.Serializable

@Serializable
class UserResponse(
    val id:Int?,
    val username:String?,
    val name:String?,
    val surname:String?
)