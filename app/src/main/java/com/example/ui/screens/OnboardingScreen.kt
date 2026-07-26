package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

// Gold Palette
val GoldCore = Color(0xFFC8A951)
val GoldLight = Color(0xFFF3E5AB)
val GoldBright = Color(0xFFDFB15B)
val GoldDark = Color(0xFF8C6D23)
val GoldGlow = Color(0xFFFFF3C4)
val LuxuryDark = Color(0xFF111111)

@Composable
fun OnboardingScreen(
    onCompleteOnboarding: () -> Unit,
    onSkip: () -> Unit = onCompleteOnboarding
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    // Continuous Infinite Animations
    val infiniteTransition = rememberInfiniteTransition(label = "gold_pulse")
    val goldRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing)
        ),
        label = "rotation"
    )
    val floatY by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("screen_onboarding")
    ) {
        // Luxury White Studio Lighting & Subtle Gold Reflections in Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height * 0.35f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GoldGlow.copy(alpha = 0.35f),
                        Color.White.copy(alpha = 0.05f),
                        Color.White
                    ),
                    center = center,
                    radius = size.width * 0.9f
                ),
                center = center,
                radius = size.width * 0.9f
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar: Brand Badge & Skip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GoldCore)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BILLIONAIRE MINDSET™",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = LuxuryDark,
                                letterSpacing = 1.2.sp
                            )
                        )
                    }
                }

                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.testTag("btn_skip_onboarding")
                ) {
                    Text(
                        text = "Skip Intro",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                    )
                }
            }

            // Main Pager Pages
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> IntroSceneOne(
                        rotation = goldRotation,
                        floatY = floatY,
                        pulseAlpha = pulseAlpha
                    )
                    1 -> VaultSceneTwo(
                        floatY = floatY,
                        pulseAlpha = pulseAlpha
                    )
                    2 -> PathSceneThree(
                        floatY = floatY
                    )
                }
            }

            // Bottom Navigation Control Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateDpAsState(
                            targetValue = if (isSelected) 32.dp else 10.dp,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "dot_width"
                        )
                        val color by animateColorAsState(
                            targetValue = if (isSelected) GoldCore else Color(0xFFE2E8F0),
                            label = "dot_color"
                        )

                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Interactive Button (Luxury Gold Liquid Press)
                val isLastPage = pagerState.currentPage == 2
                val buttonInteraction = remember { MutableInteractionSource() }
                val isPressed by buttonInteraction.collectIsPressedAsState()
                val buttonScale by animateFloatAsState(
                    targetValue = if (isPressed) 0.95f else 1f,
                    label = "btn_scale"
                )

                Surface(
                    shape = RoundedCornerShape(28.dp),
                    shadowElevation = 8.dp,
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .scale(buttonScale)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(GoldDark, GoldCore, GoldBright, GoldCore)
                            )
                        )
                        .clickable(
                            interactionSource = buttonInteraction,
                            indication = null
                        ) {
                            if (isLastPage) {
                                onCompleteOnboarding()
                            } else {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                        .testTag("btn_onboarding_next")
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Gold Shine Line
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (isLastPage) Icons.Default.AutoAwesome else Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isLastPage) "ENTER WEALTH VAULT ➔" else "NEXT DISCOVERY",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 1.sp,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SCENE 1 — THE WEALTH ENGINE AWAKENS
// ==========================================
@Composable
private fun IntroSceneOne(
    rotation: Float,
    floatY: Float,
    pulseAlpha: Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Floating 3D Gold Logo Artifact Core
        Box(
            modifier = Modifier
                .size(260.dp)
                .graphicsLayer {
                    translationY = floatY
                    rotationY = sin(Math.toRadians(rotation.toDouble())).toFloat() * 15f
                    rotationX = cos(Math.toRadians(rotation.toDouble())).toFloat() * 10f
                },
            contentAlignment = Alignment.Center
        ) {
            // Gold Holographic Halo Orbit Rings
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)

                // Outer Gold Data Ring
                drawCircle(
                    color = GoldCore.copy(alpha = pulseAlpha * 0.4f),
                    radius = size.width * 0.46f,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Particle dots on orbit
                for (i in 0 until 12) {
                    val angle = Math.toRadians((rotation + i * 30).toDouble())
                    val x = center.x + (size.width * 0.46f) * cos(angle).toFloat()
                    val y = center.y + (size.width * 0.46f) * sin(angle).toFloat()
                    drawCircle(
                        color = GoldBright,
                        radius = 4.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }

            // Soft Gold Ambient Glow Backing
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(GoldGlow, Color.Transparent)
                        )
                    )
            )

            // Official Logo Image in 3D Gold Emblem Frame
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 16.dp,
                border = androidx.compose.foundation.BorderStroke(3.dp, GoldCore),
                modifier = Modifier.size(160.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_billionaire_logo),
                        contentDescription = "Billionaire Mindset Emblem",
                        modifier = Modifier
                            .size(115.dp)
                            .padding(8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Headline & Subtitle
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = GoldGlow.copy(alpha = 0.5f),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight)
        ) {
            Text(
                text = "✨ EXCLUSIVE WEALTH SYSTEM",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldDark,
                    letterSpacing = 1.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Your Wealth Journey\nBegins Here",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = LuxuryDark,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                lineHeight = 36.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Master the mindset, high-value skills, and proven strategies behind financial intelligence and digital success.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                fontSize = 15.sp
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

// ==========================================
// SCENE 2 — THE WEALTH MINDSET & DIGITAL VAULT
// ==========================================
@Composable
private fun VaultSceneTwo(
    floatY: Float,
    pulseAlpha: Float
) {
    var dragAngleX by remember { mutableFloatStateOf(0f) }
    var dragAngleY by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Interactive Crystal Brain / Neural Intelligence Sphere
        Box(
            modifier = Modifier
                .size(260.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        dragAngleX += dragAmount.x * 0.5f
                        dragAngleY += dragAmount.y * 0.5f
                    }
                }
                .graphicsLayer {
                    translationY = floatY
                    rotationY = dragAngleX
                    rotationX = dragAngleY
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.width * 0.38f

                // Gold Neural Node Network Connections
                val nodes = listOf(
                    Offset(center.x - radius * 0.5f, center.y - radius * 0.4f),
                    Offset(center.x + radius * 0.5f, center.y - radius * 0.3f),
                    Offset(center.x, center.y),
                    Offset(center.x - radius * 0.6f, center.y + radius * 0.4f),
                    Offset(center.x + radius * 0.4f, center.y + radius * 0.5f)
                )

                // Lines connecting nodes
                for (i in nodes.indices) {
                    for (j in i + 1 until nodes.size) {
                        drawLine(
                            color = GoldCore.copy(alpha = 0.4f),
                            start = nodes[i],
                            end = nodes[j],
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }

                // Glowing Node Circles
                nodes.forEach { node ->
                    drawCircle(
                        color = GoldBright,
                        radius = 6.dp.toPx(),
                        center = node
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 3.dp.toPx(),
                        center = node
                    )
                }
            }

            // Glass Crystal Central Core Sphere
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 20.dp,
                border = androidx.compose.foundation.BorderStroke(2.dp, GoldCore),
                modifier = Modifier.size(150.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Wealth Brain Intelligence",
                        tint = GoldCore,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // Touch gesture prompt badge
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = LuxuryDark,
                shadowElevation = 6.dp,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = null,
                        tint = GoldCore,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TOUCH TO ROTATE VAULT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Headline & Subtitle
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = GoldGlow.copy(alpha = 0.5f),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight)
        ) {
            Text(
                text = "🧠 WEALTH PSYCHOLOGY ENGINE",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldDark,
                    letterSpacing = 1.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Think Like Builders.\nCreate Like Entrepreneurs.",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = LuxuryDark,
                textAlign = TextAlign.Center,
                fontSize = 26.sp,
                lineHeight = 34.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Develop the high-net-worth mindset required to identify high-income digital opportunities and scale enterprise assets.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                fontSize = 15.sp
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

// ==========================================
// SCENE 3 — THE ENTREPRENEUR PATH & MILESTONES
// ==========================================
@Composable
private fun PathSceneThree(
    floatY: Float
) {
    val steps = listOf(
        "1. Mindset" to "Rewire financial beliefs & psychology",
        "2. Knowledge" to "Master digital business models & AI",
        "3. High-Value Skills" to "Acquire monetizable expertise",
        "4. Execution & Growth" to "Build streams & land high-paying roles"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Milestone Staircase Steps Visual Card
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 12.dp,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldLight),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { translationY = floatY * 0.5f }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "THE WEALTH BLUEPRINT",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = LuxuryDark,
                            letterSpacing = 1.sp
                        )
                    )

                    Surface(
                        shape = CircleShape,
                        color = GoldCore
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                steps.forEachIndexed { index, (title, desc) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(GoldGlow),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GoldDark
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Headline & Subtitle
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = GoldGlow.copy(alpha = 0.5f),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight)
        ) {
            Text(
                text = "🚀 UNLOCK YOUR POTENTIAL",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldDark,
                    letterSpacing = 1.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Build The Future\nYou Imagine",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = LuxuryDark,
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                lineHeight = 36.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Learn directly from high-earning mentors. Execute actionable strategies. Scale your income.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                fontSize = 15.sp
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
