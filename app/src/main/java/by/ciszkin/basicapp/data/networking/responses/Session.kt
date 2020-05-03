package by.ciszkin.basicapp.data.networking.responses


import com.google.gson.annotations.SerializedName

data class Session(
    val lastLogin: Long,
    val objectId: String,
    val userStatus: String,
    @SerializedName("user-token")
    val userToken: String
)