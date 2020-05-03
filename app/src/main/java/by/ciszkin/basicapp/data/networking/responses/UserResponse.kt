package by.ciszkin.basicapp.data.networking.responses

data class UserResponse(
    val blUserLocale: String,
    val created: Long,
    val email: String,
    val name: String,
    val objectId: String,
    val ownerId: String,
    val updated: Long,
    val userStatus: String
)