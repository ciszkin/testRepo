package by.ciszkin.basicapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.ciszkin.basicapp.data.db.entity.Resource
import kotlinx.coroutines.Deferred

@Dao
interface ResourceDao {

    @Insert
    suspend fun addResources(resources: List<Resource>)

    @Query("SELECT * FROM Resource")
    suspend fun getResources() : List<Resource>

    @Query("SELECT COUNT(objectId) FROM Resource")
    suspend fun getResourcesCount()
}