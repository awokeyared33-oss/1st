package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class JobOpportunityItem(
    val id: String,
    val companyName: String,
    val companyLogoBg: Color,
    val location: String = "Bangalore",
    val isTrusted: Boolean = true,
    val jobTitle: String,
    val salaryRange: String,
    val tags: List<String>,
    val isBookmarked: Boolean,
    val applicantsCount: String = "-12",
    val originalCourseId: String? = null
)

@Composable
fun JobOpportunityCard(
    item: JobOpportunityItem,
    onClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("job_card_${item.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header Row: Company Logo + Name & Location + Bookmark
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Company / Provider Logo Badge
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(item.companyLogoBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = item.companyName,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.companyName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 15.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.location,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(TextMuted)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (item.isTrusted) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Trusted",
                                tint = SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Trusted",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }

                // Bookmark Icon Button
                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (item.isBookmarked) DangerRed.copy(alpha = 0.1f) else InactivePillBg)
                ) {
                    Icon(
                        imageVector = if (item.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (item.isBookmarked) DangerRed else TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Content: Job Title, Salary & Tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.jobTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.salaryRange,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item.tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LightBlueGlow
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer Row: Overlapping Avatars + Applicant Count & Apply Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Overlapping avatars
                    Box(
                        modifier = Modifier
                            .width(68.dp)
                            .height(28.dp)
                    ) {
                        val avatarBgs = listOf(Color(0xFF93C5FD), Color(0xFFC084FC), Color(0xFFFCA5A5))
                        avatarBgs.forEachIndexed { index, color ->
                            Box(
                                modifier = Modifier
                                    .offset(x = (index * 16).dp)
                                    .size(26.dp)
                                    .border(1.5.dp, Color.White, CircleShape)
                                    .clip(CircleShape)
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = item.applicantsCount,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                }

                // Apply Button with Soft Blue Gradient Glow
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Transparent,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFF0F7FD), Color(0xFFE3F2FD))
                            )
                        )
                        .clickable { onClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Apply",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.NorthEast,
                            contentDescription = "Apply",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
