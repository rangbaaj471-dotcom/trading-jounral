package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ApexError
import com.example.ui.theme.ApexOnSurface
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexOutlineVariant
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexSurfaceHigh

data class ChartPoint(val label: String, val value: Float, val displayValue: String)

@Composable
fun EquityCurveChart(
    modifier: Modifier = Modifier,
    isCumulative: Boolean = true
) {
    val cumulativePoints = remember {
        listOf(
            ChartPoint("Oct 1", 10000f, "$10,000.00"),
            ChartPoint("Oct 5", 10450f, "$10,450.00"),
            ChartPoint("Oct 8", 10200f, "$10,200.00"),
            ChartPoint("Oct 12", 11100f, "$11,100.00"),
            ChartPoint("Oct 15", 11650f, "$11,650.00"),
            ChartPoint("Oct 19", 12175f, "$12,175.00"),
            ChartPoint("Oct 22", 11925f, "$11,925.00"),
            ChartPoint("Oct 26", 12700f, "$12,700.00"),
            ChartPoint("Oct 29", 13420.5f, "$13,420.50")
        )
    }

    val dailyPoints = remember {
        listOf(
            ChartPoint("Oct 1", 0f, "$0.00"),
            ChartPoint("Oct 5", 450f, "+$450.00"),
            ChartPoint("Oct 8", -250f, "-$250.00"),
            ChartPoint("Oct 12", 900f, "+$900.00"),
            ChartPoint("Oct 15", 550f, "+$550.00"),
            ChartPoint("Oct 19", 525f, "+$525.00"),
            ChartPoint("Oct 22", -250f, "-$250.00"),
            ChartPoint("Oct 26", 775f, "+$775.00"),
            ChartPoint("Oct 29", 720.5f, "+$720.50")
        )
    }

    val points = if (isCumulative) cumulativePoints else dailyPoints
    var selectedPointIndex by remember { mutableStateOf<Int?>(null) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(isCumulative) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(700))
    }

    val minValue = if (isCumulative) 9500f else -400f
    val maxValue = if (isCumulative) 14000f else 1100f

    Column(modifier = modifier.testTag("equity_curve_chart")) {
        // Selected Point Tooltip Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val currentPoint = selectedPointIndex?.let { points[it] } ?: points.last()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(ApexSecondary, RoundedCornerShape(4.dp))
                )
                Text(
                    text = "  ${currentPoint.label}: ",
                    color = ApexOutline,
                    fontSize = 12.sp
                )
                Text(
                    text = currentPoint.displayValue,
                    color = if (currentPoint.value >= (if (isCumulative) 10000f else 0f)) ApexSecondary else ApexError,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = if (isCumulative) "+34.2% Return" else "Peak Daily: +$900",
                color = ApexSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Chart Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .pointerInput(points) {
                        detectTapGestures { offset ->
                            val stepX = size.width / (points.size - 1)
                            val index = (offset.x / stepX).toInt().coerceIn(0, points.lastIndex)
                            selectedPointIndex = index
                        }
                    }
            ) {
                val w = size.width
                val h = size.height
                val paddingBottom = 24.dp.toPx()
                val effectiveHeight = h - paddingBottom

                // Draw horizontal grid lines
                val gridLevels = 4
                for (i in 0..gridLevels) {
                    val y = effectiveHeight * (i.toFloat() / gridLevels)
                    drawLine(
                        color = ApexOutlineVariant.copy(alpha = 0.35f),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1f
                    )
                }

                // Compute point coordinates
                val stepX = w / (points.size - 1)
                val coords = points.mapIndexed { idx, pt ->
                    val normY = (pt.value - minValue) / (maxValue - minValue)
                    val y = effectiveHeight - (normY * effectiveHeight * progress.value)
                    Offset(idx * stepX, y)
                }

                // Fill Path
                if (coords.isNotEmpty()) {
                    val fillPath = Path().apply {
                        moveTo(coords.first().x, coords.first().y)
                        for (i in 1 until coords.size) {
                            val prev = coords[i - 1]
                            val curr = coords[i]
                            val cx1 = (prev.x + curr.x) / 2f
                            cubicTo(cx1, prev.y, cx1, curr.y, curr.x, curr.y)
                        }
                        lineTo(coords.last().x, effectiveHeight)
                        lineTo(coords.first().x, effectiveHeight)
                        close()
                    }

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                ApexSecondary.copy(alpha = 0.28f),
                                ApexSecondary.copy(alpha = 0.02f)
                            ),
                            startY = 0f,
                            endY = effectiveHeight
                        )
                    )

                    // Stroke Path
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
                        color = ApexSecondary,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw glowing circles on points
                    coords.forEachIndexed { i, pt ->
                        val isSelected = (selectedPointIndex == i) || (selectedPointIndex == null && i == coords.lastIndex)
                        if (isSelected) {
                            drawCircle(
                                color = ApexSecondary.copy(alpha = 0.4f),
                                radius = 7.dp.toPx(),
                                center = pt
                            )
                            drawCircle(
                                color = ApexSecondary,
                                radius = 4.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }
            }
        }

        // X-Axis Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Oct 1", "Oct 8", "Oct 15", "Oct 22", "Oct 29").forEach { label ->
                Text(
                    text = label,
                    color = ApexOutline,
                    fontSize = 10.sp
                )
            }
        }
    }
}
