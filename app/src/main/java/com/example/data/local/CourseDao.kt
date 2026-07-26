package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT * FROM saved_courses")
    fun getAllSavedCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM saved_courses WHERE courseId = :id LIMIT 1")
    suspend fun getCourseState(id: String): CourseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(course: CourseEntity)

    @Query("DELETE FROM saved_courses WHERE courseId = :id")
    suspend fun delete(id: String)
}
