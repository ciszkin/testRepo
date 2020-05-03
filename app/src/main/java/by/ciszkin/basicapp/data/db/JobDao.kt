package by.ciszkin.basicapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.ciszkin.basicapp.data.db.entity.DbJob

@Dao
interface JobDao {

    @Insert
    suspend fun addJobs(jobs: List<DbJob>)

    @Query("SELECT * FROM DbJob")
    suspend fun getJobs() : List<DbJob>

    @Query("SELECT COUNT(objectId) FROM DbJob")
    suspend fun getJobsCount() : Int
}