package by.ciszkin.basicapp.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbResource(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val objectId: String,
    val type: Int,
    val units: Int,
    val price: Double,
    val updated: Long
)