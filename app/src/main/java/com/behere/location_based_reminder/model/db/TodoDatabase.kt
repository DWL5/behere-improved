package com.behere.location_based_reminder.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "my-location-database"

/**
 * Database for storing all location data.
 */
@Database(entities = [Todo::class], version = 2)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun locationDao(): TodoDao

    companion object {
        // For Singleton instantiation
        @Volatile private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): TodoDatabase {
            return Room.databaseBuilder(
                context,
                TodoDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
