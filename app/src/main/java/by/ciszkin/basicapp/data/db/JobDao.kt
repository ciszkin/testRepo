package by.ciszkin.basicapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.ciszkin.basicapp.data.db.entity.Job
import kotlinx.coroutines.Deferred

@Dao
interface JobDao {

    @Insert
    suspend fun addJobs(jobs: List<Job>)

    @Query("SELECT * FROM Job")
    suspend fun getJobs() : List<Job>

    @Query("SELECT COUNT(objectId) FROM Job")
    suspend fun getJobsCount()
}