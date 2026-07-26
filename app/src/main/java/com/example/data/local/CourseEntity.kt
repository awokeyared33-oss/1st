package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_courses")
data class CourseEntity(
    @PrimaryKey val courseId: String,
    val isFavorite: Boolean = false,
    val isEnrolled: Boolean = false,
    val progressPercentage: Int = 0,
    val completedLessonIds: String = "", // Comma-separated lesson IDs
    val lastAccessedTimestamp: Long = System.currentTimeMillis()
)
