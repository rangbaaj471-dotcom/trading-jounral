package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ApexError
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexOutlineVariant

@Composable
fun DrawdownChart(
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(800))
    }

    val ddPoints = remember {
        listOf(0.0f, -0.5f, -1.2f, -0.8f, -2.4f, -4.1f, -2.9f, -1.5f, -0.6f)
    }

    Column(modifier = modifier.testTag("drawdown_chart")) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Max Drawdown Curve",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Peak-to-trough decline analysis",
                    color = ApexOutline,
                    fontSize = 11.sp
                )
            }
            Text(
                text = "Max: -4.1%",
                color = ApexError,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(top = 10.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                // Baseline y = 0
                val baselineY = 16f
                drawLine(
                    color = ApexOutlineVariant.copy(alpha = 0.5f),
                    start = Offset(0f, baselineY),
                    end = Offset(w, baselineY),
                    strokeWidth = 1.5f
                )

                val stepX = w / (ddPoints.size - 1)
                val maxAbs = 5.0f
                val effectiveH = h - baselineY - 10f

                val coords = ddPoints.mapIndexed { i, dd ->
                    val y = baselineY + (Math.abs(dd) / maxAbs) * effectiveH * progress.value
                    Offset(i * stepX, y)
                }

                if (coords.isNotEmpty()) {
                    val fillPath = Path().apply {
                        moveTo(coords.first().x, coords.first().y)
                        for (i in 1 until coords.size) {
                            val prev = coords[i - 1]
                            val curr = coords[i]
                            val cx1 = (prev.x + curr.x) / 2f
                            cubicTo(cx1, prev.y, cx1, curr.y, curr.x, curr.y)
                        }
                        lineTo(coords.last().x, baselineY)
                        lineTo(coords.first().x, baselineY)
                        close()
                    }

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ApexError.copy(alpha = 0.05f),
                                ApexError.copy(alpha = 0.35f)
                            ),
                            startY = baselineY,
                            endY = h
                        )
                    )

                    val strokePath = Path().apply {
                        moveTo(coords.first().x, coords.first().y)
                        for (i in 1 until coords.size) {
                            val prev = coords[i - 1]
                            val curr = coords[i]
                            val cx1 = (prev.x + curr.x) / 2f
                            cubicTo(cx1, prev.y, cx1, curr.y, curr.x, curr.y)
                        }
                    }

                    drawPath(
                        path = strokePath,
                        color = ApexError,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw marker at max drawdown (index 5)
                    if (coords.size > 5) {
                        val maxPt = coords[5]
                        drawCircle(
                            color = ApexError.copy(alpha = 0.35f),
                            radius = 6.dp.toPx(),
                            center = maxPt
                        )
                        drawCircle(
                            color = ApexError,
                            radius = 3.5.dp.toPx(),
                            center = maxPt
                        )
                    }
                }
            }
        }
    }
}
