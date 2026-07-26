package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.CategoryModel
import com.example.data.CourseModel
import com.example.data.PromoBannerModel
import com.example.ui.components.*
import com.example.ui.theme.HeaderBlue
import com.example.ui.theme.SurfaceWhite

@Composable
fun HomeScreen(
    greeting: String,
    userName: String,
    searchQuery: String,
    unreadNotifications: Boolean,
    categories: List<CategoryModel>,
    selectedCategoryId: String?,
    promoBanners: List<PromoBannerModel>,
    courses: List<CourseModel>,
    onSearchQueryChange: (String) -> Unit,
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onFilterClick: () -> Unit,
    onCategoryClick: (CategoryModel) -> Unit,
    onCourseClick: (CourseModel) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .testTag("screen_home")
    ) {
        // Background Header Blue Banner Region
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .background(HeaderBlue)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header Section
            HeaderSection(
                greeting = greeting,
                userName = userName,
                searchQuery = searchQuery,
                unreadNotifications = unreadNotifications,
                onSearchQueryChange = onSearchQueryChange,
                onMenuClick = onMenuClick,
                onNotificationClick = onNotificationClick,
                onFilterClick = onFilterClick
            )

            // Curved White Container Body
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = SurfaceWhite
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp)
                ) {
                    // Promotional Banner Carousel
                    item {
                        PromoBannerCarousel(
                            banners = promoBanners,
                            onBannerClick = { banner ->
                                val matchingCourse = courses.firstOrNull()
                                if (matchingCourse != null) onCourseClick(matchingCourse)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Top Category Section
                    item {
                        SectionHeader(
                            title = "Top Category",
                            onSeeAllClick = { onSeeAllClick("Category") }
                        )
                        CategoryList(
                            categories = categories,
                            selectedCategoryId = selectedCategoryId,
                            onCategoryClick = onCategoryClick
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Popular Courses Section
                    item {
                        SectionHeader(
                            title = "Popular Courses",
                            onSeeAllClick = { onSeeAllClick("Popular") }
                        )

                        val filteredCourses = if (selectedCategoryId != null) {
                            courses.filter { it.categoryId == selectedCategoryId }
                        } else {
                            courses
                        }

                        PopularCoursesList(
                            courses = filteredCourses,
                            onCourseClick = onCourseClick,
                            onFavoriteToggle = onFavoriteToggle
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Recent Course Section
                    item {
                        SectionHeader(
                            title = "Recent Course",
                            onSeeAllClick = { onSeeAllClick("Recent") }
                        )
                        RecentCoursesList(
                            courses = courses,
                            onCourseClick = onCourseClick
                        )
                    }
                }
            }
        }
    }
}
