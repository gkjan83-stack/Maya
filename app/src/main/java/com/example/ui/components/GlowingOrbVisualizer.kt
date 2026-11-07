package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MayaCyanAccent
import com.example.ui.theme.MayaPinkPrimary
import com.example.ui.theme.MayaPinkSecondary
import com.example.ui.theme.MayaPurpleLight
import com.example.ui.theme.MayaPurplePrimary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GlowingOrbVisualizer(
    isListening: Boolean,
    isSpeaking: Boolean,
    audioRmsLevel: Float,
    statusText: String,
    assistantName: String,
    onOrbClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb_pulse")

    // Breathing pulse for idle
    val idlePulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idle_pulse"
    )

    // Fast pulse for speaking / listening
    val activePulse by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "active_pulse"
    )

    // Rotation for ambient rings
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambient_rotation"
    )

    val currentScale = when {
        isListening -> 1.0f + (audioRmsLevel * 0.35f)
        isSpeaking -> activePulse
        else -> idlePulse
    }

    Column(
        modifier = modifier.padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(190.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onOrbClick
                )
                .testTag("ai_voice_orb"),
            contentAlignment = Alignment.Center
        ) {
            // Animated Canvas Ripples
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.minDimension / 2.8f

                // Outer ambient glow ring
                val outerColor = when {
                    isListening -> MayaCyanAccent.copy(alpha = 0.4f)
                    isSpeaking -> MayaPinkPrimary.copy(alpha = 0.45f)
                    else -> MayaPurpleLight.copy(alpha = 0.25f)
                }

                drawCircle(
                    color = outerColor,
                    radius = baseRadius * currentScale * 1.35f,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Middle soundwave ring with dynamic nodes
                val waveColor = when {
                    isListening -> MayaCyanAccent.copy(alpha = 0.65f)
                    isSpeaking -> MayaPinkSecondary.copy(alpha = 0.7f)
                    else -> MayaPurplePrimary.copy(alpha = 0.35f)
                }

                val ringRadius = baseRadius * currentScale * 1.15f
                drawCircle(
                    color = waveColor,
                    radius = ringRadius,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )

                // Orbiting audio particles
                val particleCount = if (isSpeaking || isListening) 8 else 4
                for (i in 0 until particleCount) {
                    val angle = Math.toRadians((rotationAngle + (i * 360.0 / particleCount))).toFloat()
                    val px = center.x + (ringRadius * cos(angle.toDouble())).toFloat()
                    val py = center.y + (ringRadius * sin(angle.toDouble())).toFloat()
                    val pColor = if (i % 2 == 0) MayaPinkPrimary else MayaCyanAccent
                    drawCircle(
                        color = pColor,
                        radius = (if (isSpeaking || isListening) 5.dp else 3.dp).toPx(),
                        center = Offset(px, py)
                    )
                }
            }

            // Central Glowing Sphere
            val orbGradient = when {
                isListening -> Brush.radialGradient(
                    colors = listOf(
                        MayaCyanAccent,
                        Color(0xFF00B0FF),
                        Color(0xFF006064),
                        Color(0xFF0F081D)
                    )
                )
                isSpeaking -> Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF80AB),
                        MayaPinkPrimary,
                        MayaPurplePrimary,
                        Color(0xFF0F081D)
                    )
                )
                else -> Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF8DA1),
                        MayaPinkPrimary,
                        Color(0xFF4A148C),
                        Color(0xFF0F081D)
                    )
                )
            }

            Box(
                modifier = Modifier
                    .size(116.dp)
                    .scale(currentScale)
                    .shadow(
                        elevation = if (isSpeaking || isListening) 24.dp else 12.dp,
                        shape = CircleShape,
                        spotColor = if (isListening) MayaCyanAccent else MayaPinkPrimary
                    )
                    .clip(CircleShape)
                    .background(orbGradient)
                    .border(
                        width = 2.5.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.7f),
                                MayaPinkPrimary,
                                MayaCyanAccent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Heart & Audio Symbol in Core
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = when {
                            isListening -> Icons.Default.Mic
                            isSpeaking -> Icons.Default.GraphicEq
                            else -> Icons.Default.Favorite
                        },
                        contentDescription = "Assistant State",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = assistantName,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Status Pill with Glowing Background
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.border(
                1.dp,
                if (isListening) MayaCyanAccent.copy(alpha = 0.5f)
                else if (isSpeaking) MayaPinkPrimary.copy(alpha = 0.5f)
                else Color.White.copy(alpha = 0.1f),
                CircleShape
            )
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = statusText,
                    color = when {
                        isListening -> MayaCyanAccent
                        isSpeaking -> MayaPinkSecondary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
