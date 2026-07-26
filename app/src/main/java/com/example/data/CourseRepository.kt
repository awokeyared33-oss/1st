package com.example.data

import com.example.R
import com.example.data.local.CourseDao
import com.example.data.local.CourseEntity
import com.example.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CourseRepository(private val courseDao: CourseDao) {

    // Mock initial categories based on visual spec
    val categories: List<CategoryModel> = listOf(
        CategoryModel(
            id = "graphics",
            title = "Graphics\nDesign",
            iconName = "brush",
            bgColor = CatBlueBg,
            iconColor = CatBlueIcon,
            courseCount = 18
        ),
        CategoryModel(
            id = "web",
            title = "Web\nDesign",
            iconName = "computer",
            bgColor = CatPurpleBg,
            iconColor = CatPurpleIcon,
            courseCount = 24
        ),
        CategoryModel(
            id = "writing",
            title = "Article\nWriting",
            iconName = "edit",
            bgColor = CatPinkBg,
            iconColor = CatPinkIcon,
            courseCount = 12
        ),
        CategoryModel(
            id = "uiux",
            title = "UI/UX\nDesign",
            iconName = "design_services",
            bgColor = CatGreenBg,
            iconColor = CatGreenIcon,
            courseCount = 35
        ),
        CategoryModel(
            id = "marketing",
            title = "Digital\nMarket",
            iconName = "campaign",
            bgColor = CatOrangeBg,
            iconColor = CatOrangeIcon,
            courseCount = 20
        ),
        CategoryModel(
            id = "data",
            title = "Data\nScience",
            iconName = "analytics",
            bgColor = CatYellowBg,
            iconColor = CatYellowIcon,
            courseCount = 15
        )
    )

    // Initial course list
    private val defaultCourses: List<CourseModel> = listOf(
        CourseModel(
            id = "c1",
            title = "User Interface Design for Beginner",
            price = "$37.00",
            lessonsCount = 20,
            duration = "5h 20m",
            rating = 4.9,
            reviews = 27,
            coverResId = R.drawable.img_course_uiux_1785081611498,
            categoryId = "uiux",
            description = "Master the fundamentals of UI design, layout hierarchy, color systems, and modern component prototyping using industry standard techniques.",
            instructorName = "Leslie Alexander",
            instructorRole = "Principal UI Specialist",
            lessons = listOf(
                LessonModel("l1", "01. Introduction to UI Systems", "12:30", true, true),
                LessonModel("l2", "02. Color Theory & Typography Pairs", "18:45", true, true),
                LessonModel("l3", "03. Layout Grid & Spacing Masterclass", "24:10", true, true),
                LessonModel("l4", "04. Creating Interactive Prototypes", "15:20", true, false),
                LessonModel("l5", "05. Mobile Micro-interactions", "20:00", true, false)
            )
        ),
        CourseModel(
            id = "c2",
            title = "Your Skills Explore Digital Marketing",
            price = "$37.00",
            lessonsCount = 20,
            duration = "6h 45m",
            rating = 4.9,
            reviews = 27,
            coverResId = R.drawable.img_course_marketing_1785081624164,
            categoryId = "marketing",
            description = "Unlock growth strategies, SEO analytics, social media branding campaigns, and high-converting marketing funnels.",
            instructorName = "Arlene McCoy",
            instructorRole = "Growth Marketing Director",
            lessons = listOf(
                LessonModel("l21", "01. Fundamentals of Growth Marketing", "10:15", true, true),
                LessonModel("l22", "02. Audience Targeting & Personas", "16:00", true, false),
                LessonModel("l23", "03. SEO & Organic Traffic Engine", "22:30", true, false)
            )
        ),
        CourseModel(
            id = "c3",
            title = "Modern Responsive Web Design 2026",
            price = "$45.00",
            lessonsCount = 18,
            duration = "4h 15m",
            rating = 4.8,
            reviews = 34,
            coverResId = R.drawable.img_promo_banner_1_1785081596380,
            categoryId = "web",
            description = "Build clean, accessible, fluid web applications with cutting edge CSS layouts, flexbox, grid, and adaptive component frameworks.",
            instructorName = "Cameron Williamson",
            instructorRole = "Senior Web Architect",
            lessons = listOf(
                LessonModel("l31", "01. HTML5 & Semantic Elements", "14:00", true, true),
                LessonModel("l32", "02. CSS Grid vs Flexbox Deep Dive", "21:10", true, true)
            )
        ),
        CourseModel(
            id = "c4",
            title = "Article & Content Writing Mastery",
            price = "$29.00",
            lessonsCount = 14,
            duration = "3h 50m",
            rating = 4.7,
            reviews = 19,
            coverResId = R.drawable.img_course_marketing_1785081624164,
            categoryId = "writing",
            description = "Craft compelling narratives, persuasive copywriting, and SEO optimized articles that resonate with global audiences.",
            instructorName = "Esther Howard",
            instructorRole = "Senior Editorial Strategist",
            lessons = listOf(
                LessonModel("l41", "01. Copywriting Hooks & Headlines", "11:45", true, false),
                LessonModel("l42", "02. Structuring Longform Content", "19:20", true, false)
            )
        )
    )

    // Banners for header slider
    val promoBanners = listOf(
        PromoBannerModel(
            id = "b1",
            title = "What Would you like\nto learn today?",
            subtitle = "Explore over 1,200+ top courses",
            buttonText = "Get Started",
            bgStartColor = PrimaryBlue,
            bgEndColor = DarkBlue,
            imageResId = R.drawable.img_promo_banner_1_1785081596380
        ),
        PromoBannerModel(
            id = "b2",
            title = "Upgrade Your Skills\nWith Live Interactive Sessions",
            subtitle = "Get certified by top mentors",
            buttonText = "Explore Now",
            bgStartColor = AccentOrange,
            bgEndColor = PrimaryBlue,
            imageResId = R.drawable.img_course_uiux_1785081611498
        )
    )

    // Notifications mock
    val initialNotifications = listOf(
        NotificationModel("n1", "Course Updated", "New lessons added to UI Design for Beginner", "10m ago", false),
        NotificationModel("n2", "Certificate Earned", "Congratulations! You completed Web Fundamentals", "2h ago", false),
        NotificationModel("n3", "Weekly Progress", "You hit a 5-day learning streak!", "1d ago", true)
    )

    // Flow of courses combined with Room local DB saved state
    fun getCoursesFlow(): Flow<List<CourseModel>> {
        return courseDao.getAllSavedCourses().map { savedList ->
            val savedMap = savedList.associateBy { it.courseId }
            defaultCourses.map { course ->
                val savedEntity = savedMap[course.id]
                if (savedEntity != null) {
                    val completedIds = savedEntity.completedLessonIds.split(",").filter { it.isNotBlank() }.toSet()
                    val updatedLessons = course.lessons.map { lesson ->
                        if (lesson.id in completedIds) lesson.copy(isCompleted = true) else lesson
                    }
                    course.copy(
                        isFavorite = savedEntity.isFavorite,
                        progressPercentage = savedEntity.progressPercentage,
                        lessons = updatedLessons
                    )
                } else {
                    course
                }
            }
        }
    }

    suspend fun toggleFavorite(courseId: String) {
        val current = courseDao.getCourseState(courseId)
        if (current == null) {
            courseDao.insertOrUpdate(CourseEntity(courseId = courseId, isFavorite = true))
        } else {
            courseDao.insertOrUpdate(current.copy(isFavorite = !current.isFavorite))
        }
    }

    suspend fun enrollCourse(courseId: String) {
        val current = courseDao.getCourseState(courseId)
        if (current == null) {
            courseDao.insertOrUpdate(CourseEntity(courseId = courseId, isEnrolled = true, progressPercentage = 10))
        } else {
            courseDao.insertOrUpdate(current.copy(isEnrolled = true, progressPercentage = if (current.progressPercentage == 0) 10 else current.progressPercentage))
        }
    }

    suspend fun toggleLessonCompletion(courseId: String, lessonId: String) {
        val current = courseDao.getCourseState(courseId)
        val defaultCourse = defaultCourses.find { it.id == courseId } ?: return
        val currentCompletedIds = current?.completedLessonIds?.split(",")?.filter { it.isNotBlank() }?.toMutableSet() ?: mutableSetOf()

        if (lessonId in currentCompletedIds) {
            currentCompletedIds.remove(lessonId)
        } else {
            currentCompletedIds.add(lessonId)
        }

        val progress = if (defaultCourse.lessons.isNotEmpty()) {
            ((currentCompletedIds.size.toFloat() / defaultCourse.lessons.size.toFloat()) * 100).toInt()
        } else 0

        val newEntity = CourseEntity(
            courseId = courseId,
            isFavorite = current?.isFavorite ?: false,
            isEnrolled = true,
            progressPercentage = progress,
            completedLessonIds = currentCompletedIds.joinToString(",")
        )
        courseDao.insertOrUpdate(newEntity)
    }
}
