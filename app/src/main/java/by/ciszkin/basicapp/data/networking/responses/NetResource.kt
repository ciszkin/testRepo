package by.ciszkin.basicapp.data.networking.responses


import com.google.gson.annotations.SerializedName

data class NetResource(
    val created: Long,
    @SerializedName("internal_name")
    val internalName: String,
    @SerializedName("name_ru")
    val name: String,
    val objectId: String,
    val type: Int,
    val units: Int,
    val updated: Long
)