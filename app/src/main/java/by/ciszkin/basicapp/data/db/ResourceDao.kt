package by.ciszkin.basicapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.ciszkin.basicapp.data.db.entity.DbResource

@Dao
interface ResourceDao {

    @Insert
    suspend fun addResources(resources: List<DbResource>)

    @Query("SELECT * FROM DbResource")
    suspend fun getResources() : List<DbResource>

    @Query("SELECT COUNT(objectId) FROM DbResource")
    suspend fun getResourcesCount() : Int
}