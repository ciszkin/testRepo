package by.ciszkin.basicapp.data.db.entity



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbJob(
    val created: Long,
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val objectId: String,
    val resources: String,
    val resourcesRates: String,
    val surface: Int,
    val type: Int,
    val units: Int,
    val updated: Long,
    val workflow: String,
    val price: Double
)