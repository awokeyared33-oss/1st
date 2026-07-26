package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategoryModel
import com.example.data.CourseModel
import com.example.ui.components.JobOpportunityCard
import com.example.ui.components.JobOpportunityItem
import com.example.ui.theme.*

@Composable
fun CoursesScreen(
    searchQuery: String,
    categories: List<CategoryModel>,
    selectedCategoryId: String?,
    selectedPriceFilter: String,
    minRatingFilter: Double,
    courses: List<CourseModel>,
    onSearchQueryChange: (String) -> Unit,
    onCategoryClick: (CategoryModel) -> Unit,
    onCourseClick: (CourseModel) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Custom filter category options matching user request
    val earningCategories = listOf(
        "UI/UX Designer",
        "Graphic Designer",
        "High-Paying Skill",
        "Hybrid",
        "E-Commerce Growth",
        "AI Prompt Pro"
    )

    var activePillIndex by remember { mutableIntStateOf(0) }

    // Map course models to job/opportunity items
    val jobItems = remember(courses, searchQuery, selectedCategoryId) {
        val companyLogos = listOf(
            PrimaryBlue,
            Color(0xFF8E24AA),
            Color(0xFF10B981),
            Color(0xFFF59E0B),
            Color(0xFFEF4444),
            Color(0xFF6366F1)
        )

        courses.mapIndexed { index, course ->
            val salary = when (index % 4) {
                0 -> "₹75,000 - ₹1,20,000 / Month"
                1 -> "₹85,000 - ₹1,40,000 / Month"
                2 -> "₹1,50,000 - ₹2,20,000 / Month"
                else -> "₹95,000 - ₹1,60,000 / Month"
            }
            val tags = when (index % 3) {
                0 -> listOf("Hybrid", "+3")
                1 -> listOf("Full-time", "+2")
                else -> listOf("Remote", "+4")
            }
            val applicants = when (index % 3) {
                0 -> "-12"
                1 -> "-14"
                else -> "-8"
            }
            JobOpportunityItem(
                id = course.id,
                companyName = course.instructorName.ifBlank { "Top Company ${index + 1}" },
                companyLogoBg = companyLogos[index % companyLogos.size],
                location = if (index % 2 == 0) "Bangalore" else "Remote",
                isTrusted = true,
                jobTitle = course.title,
                salaryRange = salary,
                tags = tags,
                isBookmarked = course.isFavorite,
                applicantsCount = applicants,
                originalCourseId = course.id
            )
        }.filter { item ->
            searchQuery.isBlank() ||
                    item.jobTitle.contains(searchQuery, ignoreCase = true) ||
                    item.companyName.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
            .testTag("screen_courses")
    ) {
        // Soft Light-Blue Radial Glow Effect at the top-left
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-60).dp, y = (-60).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PrimaryBlue.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Search Header Bar
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            placeholder = {
                                Text(
                                    "Search Here...",
                                    color = TextSecondary,
                                    fontSize = 15.sp
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // Mic & Profile Avatar Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(LightBlueGlow)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Search",
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFCBD5E1)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Filter & Sort Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onFilterClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = "Filters",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Filters",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onFilterClick() }
                    ) {
                        Text(
                            text = "Recently",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Category Pills List
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(earningCategories) { index, categoryName ->
                        val isSelected = activePillIndex == index
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) PrimaryBlue else InactivePillBg,
                            modifier = Modifier.clickable {
                                activePillIndex = index
                                if (categories.isNotEmpty()) {
                                    onCategoryClick(categories[index % categories.size])
                                }
                            }
                        ) {
                            Text(
                                text = categoryName,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Results Count Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${jobItems.size * 35 + 2} Opportunities Found",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "See all >>",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        ),
                        modifier = Modifier.clickable { /* Reset query / filters */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Job / Opportunity Cards List
            if (jobItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No opportunities found matching search query.\nTry clearing filters!",
                            style = MaterialTheme.typography.bodyLarge.copy(color = TextSecondary),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(jobItems, key = { it.id }) { job ->
                    JobOpportunityCard(
                        item = job,
                        onClick = {
                            val original = courses.find { it.id == job.id }
                            if (original != null) {
                                onCourseClick(original)
                            }
                        },
                        onBookmarkToggle = {
                            onFavoriteToggle(job.id)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
