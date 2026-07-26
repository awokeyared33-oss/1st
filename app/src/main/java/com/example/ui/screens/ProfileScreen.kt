package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CourseModel
import com.example.ui.components.CourseCard
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userName: String,
    courses: List<CourseModel>,
    onCourseClick: (CourseModel) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onReplayOnboarding: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val favoriteCourses = courses.filter { it.isFavorite }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
            .testTag("screen_profile"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_user_avatar_1785081637955),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(3.dp, HeaderBlue, CircleShape)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )

                    Text(
                        text = "UI/UX Student • Premium Member",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileStatItem(value = "28h", label = "Hours Spent")
                        ProfileStatItem(value = "${courses.count { it.progressPercentage > 0 || it.isFavorite }}", label = "Enrolled")
                        ProfileStatItem(value = "2", label = "Certificates")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Favorites Section
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Bookmarked Courses (${favoriteCourses.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (favoriteCourses.isEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No bookmarked courses yet. Tap the heart icon on any course card!",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        if (favoriteCourses.isNotEmpty()) {
            items(favoriteCourses, key = { "fav_${it.id}" }) { course ->
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                    CourseCard(
                        course = course,
                        onClick = { onCourseClick(course) },
                        onFavoriteToggle = { onFavoriteToggle(course.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Settings Menu
        item {
            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Account Settings",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        ProfileSettingTile(
                            icon = Icons.Default.AutoAwesome,
                            title = "Replay Wealth Vault Onboarding",
                            onClick = onReplayOnboarding
                        )
                        Divider(color = BackgroundLight)
                        ProfileSettingTile(icon = Icons.Default.Person, title = "Edit Profile Info")
                        Divider(color = BackgroundLight)
                        ProfileSettingTile(icon = Icons.Default.Lock, title = "Security & Password")
                        Divider(color = BackgroundLight)
                        ProfileSettingTile(icon = Icons.Default.Settings, title = "App Preferences")
                        Divider(color = BackgroundLight)
                        ProfileSettingTile(icon = Icons.Default.Help, title = "Help & Support Center")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                color = PrimaryBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
private fun ProfileSettingTile(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = PrimaryBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Arrow",
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
