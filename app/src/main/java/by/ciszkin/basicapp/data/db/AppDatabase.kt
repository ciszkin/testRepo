package by.ciszkin.basicapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.ciszkin.basicapp.data.db.entity.Job
import by.ciszkin.basicapp.data.db.entity.Resource

@Database(entities = [Job::class, Resource::class], version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract fun getJobDao(): JobDao

    abstract fun getResourceDao(): ResourceDao

    companion object {
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context): AppDatabase {

            return instance
                ?: createInstance(
                    context
                ).also{
                instance = it
            }
        }

        private fun createInstance(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "example2.db"
            ).build()
    }
}