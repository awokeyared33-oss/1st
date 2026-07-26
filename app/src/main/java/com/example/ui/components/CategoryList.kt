package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategoryModel
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary

@Composable
fun CategoryList(
    categories: List<CategoryModel>,
    selectedCategoryId: String?,
    onCategoryClick: (CategoryModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .testTag("list_categories"),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(categories, key = { it.id }) { category ->
            val isSelected = category.id == selectedCategoryId
            CategoryItem(
                category = category,
                isSelected = isSelected,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
private fun CategoryItem(
    category: CategoryModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon = getCategoryIcon(category.iconName)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .clickable { onClick() }
            .testTag("cat_item_${category.id}")
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(if (isSelected) PrimaryBlue else category.bgColor)
                .then(
                    if (isSelected) Modifier.border(2.dp, PrimaryBlue, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = category.title,
                tint = if (isSelected) Color.White else category.iconColor,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category.title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isSelected) PrimaryBlue else TextPrimary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 15.sp
            ),
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

private fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "brush" -> Icons.Default.Brush
        "computer" -> Icons.Default.Computer
        "edit" -> Icons.Default.Edit
        "design_services" -> Icons.Default.DesignServices
        "campaign" -> Icons.Default.Campaign
        "analytics" -> Icons.Default.Analytics
        else -> Icons.Default.Category
    }
}
