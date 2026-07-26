package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.local.EduLearnDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class NavTab {
    HOME, COURSE, HISTORY, PROFILE
}

data class MainUiState(
    val currentTab: NavTab = NavTab.HOME,
    val selectedCategoryId: String? = null,
    val searchQuery: String = "",
    val courses: List<CourseModel> = emptyList(),
    val selectedCourse: CourseModel? = null,
    val showFilterSheet: Boolean = false,
    val showNotificationSheet: Boolean = false,
    val showFabQuickAction: Boolean = false,
    val showOnboarding: Boolean = true,
    val notifications: List<NotificationModel> = emptyList(),
    val minRatingFilter: Double = 0.0,
    val selectedPriceFilter: String = "All",
    val userName: String = "Arlene McCoy",
    val greetingText: String = "👋 Hello"
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = EduLearnDatabase.getInstance(application)
    private val repository = CourseRepository(db.courseDao())

    val categories = repository.categories
    val promoBanners = repository.promoBanners

    private val _uiState = MutableStateFlow(MainUiState(notifications = repository.initialNotifications))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCoursesFlow().collect { courses ->
                _uiState.update { currentState ->
                    val updatedSelected = currentState.selectedCourse?.let { selected ->
                        courses.find { it.id == selected.id }
                    }
                    currentState.copy(
                        courses = courses,
                        selectedCourse = updatedSelected ?: currentState.selectedCourse
                    )
                }
            }
        }
    }

    fun selectTab(tab: NavTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { currentState ->
            val newCategory = if (currentState.selectedCategoryId == categoryId) null else categoryId
            currentState.copy(selectedCategoryId = newCategory)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleFavorite(courseId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(courseId)
        }
    }

    fun enrollCourse(courseId: String) {
        viewModelScope.launch {
            repository.enrollCourse(courseId)
        }
    }

    fun toggleLessonCompletion(courseId: String, lessonId: String) {
        viewModelScope.launch {
            repository.toggleLessonCompletion(courseId, lessonId)
        }
    }

    fun openCourseDetail(course: CourseModel) {
        _uiState.update { it.copy(selectedCourse = course) }
    }

    fun closeCourseDetail() {
        _uiState.update { it.copy(selectedCourse = null) }
    }

    fun showFilterSheet(show: Boolean) {
        _uiState.update { it.copy(showFilterSheet = show) }
    }

    fun showNotificationSheet(show: Boolean) {
        _uiState.update { it.copy(showNotificationSheet = show) }
    }

    fun showFabQuickAction(show: Boolean) {
        _uiState.update { it.copy(showFabQuickAction = show) }
    }

    fun setRatingFilter(rating: Double) {
        _uiState.update { it.copy(minRatingFilter = rating) }
    }

    fun setPriceFilter(priceOption: String) {
        _uiState.update { it.copy(selectedPriceFilter = priceOption) }
    }

    fun markNotificationsAsRead() {
        _uiState.update { currentState ->
            currentState.copy(notifications = currentState.notifications.map { it.copy(isRead = true) })
        }
    }

    fun completeOnboarding() {
        _uiState.update { it.copy(showOnboarding = false) }
    }

    fun openOnboarding() {
        _uiState.update { it.copy(showOnboarding = true) }
    }
}
