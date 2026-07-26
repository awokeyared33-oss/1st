package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CourseEntity::class], version = 1, exportSchema = false)
abstract class EduLearnDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var INSTANCE: EduLearnDatabase? = null

        fun getInstance(context: Context): EduLearnDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EduLearnDatabase::class.java,
                    "edulearn_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
