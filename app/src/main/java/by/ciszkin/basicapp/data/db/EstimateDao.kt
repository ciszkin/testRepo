package by.ciszkin.basicapp.data.db

import androidx.room.*
import by.ciszkin.basicapp.data.db.entity.DbEstimate

@Dao
interface EstimateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEstimates(estimates: List<DbEstimate>)

    @Delete
    fun deleteEstimates(estimates: List<DbEstimate>)

    @Query("SELECT * FROM DbEstimate")
    fun getEstimates() : List<DbEstimate>

    @Query("SELECT COUNT(objectId) FROM DbResource")
    fun getEstimatesCount() : Int
}