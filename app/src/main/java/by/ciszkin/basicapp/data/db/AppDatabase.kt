package by.ciszkin.basicapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.ciszkin.basicapp.data.db.entity.DbEstimate
import by.ciszkin.basicapp.data.db.entity.DbJob
import by.ciszkin.basicapp.data.db.entity.DbResource

@Database(entities = [DbEstimate::class, DbJob::class, DbResource::class], version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract fun getJobDao(): JobDao

    abstract fun getResourceDao(): ResourceDao

    abstract fun getEstimateDao(): EstimateDao

    companion object {
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context): AppDatabase {

            return instance
                ?: createInstance(
                    context
                ).also {
                    instance = it
                }
        }

        private fun createInstance(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "example7.db"
            ).build()
    }
}