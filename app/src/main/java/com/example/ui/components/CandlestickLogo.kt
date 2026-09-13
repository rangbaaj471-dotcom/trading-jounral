package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ApexOnSurface
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexSecondary

@Composable
fun ApexFxLogo(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    showText: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val strokeW = (w / 16f).coerceAtLeast(2f)

            // Hexagon vertices
            val hexPath = Path().apply {
                // Top-right segment
                moveTo(w * 0.44f, h * 0.25f)
                lineTo(w * 0.50f, h * 0.22f)
                lineTo(w * 0.74f, h * 0.36f)
                lineTo(w * 0.74f, h * 0.64f)
                lineTo(w * 0.64f, h * 0.70f)
            }
            drawPath(
                path = hexPath,
                color = ApexOutline,
                style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            val hexPath2 = Path().apply {
                // Bottom-left segment
                moveTo(w * 0.56f, h * 0.75f)
                lineTo(w * 0.50f, h * 0.78f)
                lineTo(w * 0.26f, h * 0.64f)
                lineTo(w * 0.26f, h * 0.36f)
                lineTo(w * 0.36f, h * 0.30f)
            }
            drawPath(
                path = hexPath2,
                color = ApexOutline,
                style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Left Green Candlestick
            // Wick
            drawLine(
                color = ApexSecondary,
                start = Offset(w * 0.40f, h * 0.20f),
                end = Offset(w * 0.40f, h * 0.76f),
                strokeWidth = strokeW,
                cap = StrokeCap.Square
            )
            // Body
            drawRect(
                color = ApexSecondary,
                topLeft = Offset(w * 0.34f, h * 0.33f),
                size = Size(w * 0.12f, h * 0.27f),
                style = Stroke(width = strokeW)
            )

            // Right Blue Candlestick
            // Wick
            drawLine(
                color = Color(0xFF2563EB),
                start = Offset(w * 0.59f, h * 0.28f),
                end = Offset(w * 0.59f, h * 0.82f),
                strokeWidth = strokeW,
                cap = StrokeCap.Square
            )
            // Body
            drawRect(
                color = Color(0xFF2563EB),
                topLeft = Offset(w * 0.53f, h * 0.40f),
                size = Size(w * 0.12f, h * 0.27f),
                style = Stroke(width = strokeW)
            )
        }

        if (showText) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "APEXFX",
                color = ApexOnSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}
