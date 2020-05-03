package by.ciszkin.basicapp.data.networking.responses


import com.google.gson.annotations.SerializedName

data class NetJob(
    val created: Long,
    @SerializedName("name_ru")
    val name: String,
    val objectId: String,
    @SerializedName("resource_consumption")
    val resources: List<ResourceConsumption>,
    val surface: Int,
    val type: Int,
    val units: Int,
    val updated: Long,
    @SerializedName("workflow_ru")
    val workflow: String
) {
    data class ResourceConsumption(
        val rate: Double,
        val resource: String
    )
}