package by.ciszkin.basicapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbEstimate(
    @PrimaryKey(autoGenerate = false)
    val objectId: String,
    val created: Long,
    val title: String,
    val deadline: Long,
    val note: String,
    val estimateJobsRecordsString: String,
    val priceString: String
)