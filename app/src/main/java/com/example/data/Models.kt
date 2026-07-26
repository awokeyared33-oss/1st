package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.R

data class CategoryModel(
    val id: String,
    val title: String,
    val iconName: String,
    val bgColor: Color,
    val iconColor: Color,
    val courseCount: Int
)

data class LessonModel(
    val id: String,
    val title: String,
    val duration: String,
    val isVideo: Boolean = true,
    val isCompleted: Boolean = false
)

data class CourseModel(
    val id: String,
    val title: String,
    val price: String,
    val lessonsCount: Int,
    val duration: String = "4h 30m",
    val rating: Double,
    val reviews: Int,
    val coverResId: Int,
    val categoryId: String,
    val description: String,
    val instructorName: String,
    val instructorRole: String,
    val isFavorite: Boolean = false,
    val progressPercentage: Int = 0,
    val lessons: List<LessonModel> = emptyList()
)

data class NotificationModel(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false
)

data class PromoBannerModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val buttonText: String = "Get Started",
    val bgStartColor: Color,
    val bgEndColor: Color,
    val imageResId: Int
)
