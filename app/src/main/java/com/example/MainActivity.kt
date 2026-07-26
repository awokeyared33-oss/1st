package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.NavTab
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.EduLearnTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduLearnTheme {
                EduLearnApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun EduLearnApp(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            CustomBottomNavBar(
                currentTab = uiState.currentTab,
                onTabSelected = { tab -> viewModel.selectTab(tab) },
                onFabClick = { viewModel.showFabQuickAction(true) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                NavTab.HOME -> {
                    HomeScreen(
                        greeting = uiState.greetingText,
                        userName = uiState.userName,
                        searchQuery = uiState.searchQuery,
                        unreadNotifications = uiState.notifications.any { !it.isRead },
                        categories = viewModel.categories,
                        selectedCategoryId = uiState.selectedCategoryId,
                        promoBanners = viewModel.promoBanners,
                        courses = uiState.courses,
                        onSearchQueryChange = { query -> viewModel.setSearchQuery(query) },
                        onMenuClick = { viewModel.showFilterSheet(true) },
                        onNotificationClick = { viewModel.showNotificationSheet(true) },
                        onFilterClick = { viewModel.showFilterSheet(true) },
                        onCategoryClick = { category -> viewModel.selectCategory(category.id) },
                        onCourseClick = { course -> viewModel.openCourseDetail(course) },
                        onFavoriteToggle = { courseId -> viewModel.toggleFavorite(courseId) },
                        onSeeAllClick = { viewModel.selectTab(NavTab.COURSE) }
                    )
                }
                NavTab.COURSE -> {
                    CoursesScreen(
                        searchQuery = uiState.searchQuery,
                        categories = viewModel.categories,
                        selectedCategoryId = uiState.selectedCategoryId,
                        selectedPriceFilter = uiState.selectedPriceFilter,
                        minRatingFilter = uiState.minRatingFilter,
                        courses = uiState.courses,
                        onSearchQueryChange = { query -> viewModel.setSearchQuery(query) },
                        onCategoryClick = { category -> viewModel.selectCategory(category.id) },
                        onCourseClick = { course -> viewModel.openCourseDetail(course) },
                        onFavoriteToggle = { courseId -> viewModel.toggleFavorite(courseId) },
                        onFilterClick = { viewModel.showFilterSheet(true) }
                    )
                }
                NavTab.HISTORY -> {
                    HistoryScreen(
                        courses = uiState.courses,
                        onCourseClick = { course -> viewModel.openCourseDetail(course) }
                    )
                }
                NavTab.PROFILE -> {
                    ProfileScreen(
                        userName = uiState.userName,
                        courses = uiState.courses,
                        onCourseClick = { course -> viewModel.openCourseDetail(course) },
                        onFavoriteToggle = { courseId -> viewModel.toggleFavorite(courseId) }
                    )
                }
            }

            // Modal Course Detail Sheet
            uiState.selectedCourse?.let { course ->
                CourseDetailSheet(
                    course = course,
                    onDismiss = { viewModel.closeCourseDetail() },
                    onFavoriteToggle = { courseId -> viewModel.toggleFavorite(courseId) },
                    onEnroll = { courseId -> viewModel.enrollCourse(courseId) },
                    onLessonToggle = { courseId, lessonId -> viewModel.toggleLessonCompletion(courseId, lessonId) }
                )
            }

            // Filter Bottom Sheet
            if (uiState.showFilterSheet) {
                FilterBottomSheet(
                    categories = viewModel.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    selectedPriceFilter = uiState.selectedPriceFilter,
                    minRatingFilter = uiState.minRatingFilter,
                    onSelectCategory = { categoryId -> viewModel.selectCategory(categoryId) },
                    onSelectPrice = { price -> viewModel.setPriceFilter(price) },
                    onSelectRating = { rating -> viewModel.setRatingFilter(rating) },
                    onDismiss = { viewModel.showFilterSheet(false) }
                )
            }

            // Notifications Bottom Sheet
            if (uiState.showNotificationSheet) {
                NotificationBottomSheet(
                    notifications = uiState.notifications,
                    onMarkAllRead = { viewModel.markNotificationsAsRead() },
                    onDismiss = { viewModel.showNotificationSheet(false) }
                )
            }

            // Quick Action FAB Bottom Sheet
            if (uiState.showFabQuickAction) {
                QuickActionSheet(
                    onDismiss = { viewModel.showFabQuickAction(false) },
                    onResumeLastCourse = {
                        val activeCourse = uiState.courses.firstOrNull { it.progressPercentage > 0 } ?: uiState.courses.first()
                        viewModel.openCourseDetail(activeCourse)
                    },
                    onQuickSearch = {
                        viewModel.selectTab(NavTab.COURSE)
                    }
                )
            }
        }
    }
}
