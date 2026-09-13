package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.TrendingDown
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TradeEntity
import com.example.ui.theme.ApexCardBorder
import com.example.ui.theme.ApexError
import com.example.ui.theme.ApexOnPrimary
import com.example.ui.theme.ApexOnSurface
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexPrimaryContainer
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexSurfaceLow

@Composable
fun HeaderBar(
    title: String,
    onQuickTradeClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ApexSurfaceLow)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ApexFxLogo(size = 28.dp, showText = false)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    color = ApexOnSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Professional Forex Journal",
                    color = ApexOutline,
                    fontSize = 11.sp
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Quick Trade Action
            Button(
                onClick = onQuickTradeClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ApexPrimaryContainer,
                    contentColor = ApexOnPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(36.dp)
                    .testTag("quick_trade_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Trade",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Quick Trade", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Notifications
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ApexSurfaceContainer)
                    .clickable { onNotificationsClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Alerts",
                    tint = ApexOutline,
                    modifier = Modifier.size(18.dp)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 2.dp)
                        .background(ApexSecondary, CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Avatar AX
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AX",
                    color = ApexSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ActivePositionsTicker(
    openTrades: List<TradeEntity>,
    onManageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (openTrades.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ApexSurfaceContainer.copy(alpha = 0.9f))
            .border(1.dp, ApexCardBorder)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .background(ApexPrimaryContainer.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${openTrades.size} ACTIVE",
                    color = ApexPrimaryContainer,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            openTrades.take(2).forEachIndexed { index, trade ->
                if (index > 0) {
                    Text(text = " • ", color = ApexOutline, fontSize = 12.sp)
                }
                val isPositive = trade.pnl >= 0
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${trade.pair} ${trade.direction} ",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${if (isPositive) "+" else ""}$${String.format("%.2f", trade.pnl)} (${if (isPositive) "+" else ""}${trade.pips}p)",
                        color = if (isPositive) ApexSecondary else ApexError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = "Manage",
            color = ApexPrimaryContainer,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .clickable { onManageClick() }
                .padding(start = 8.dp)
                .testTag("manage_open_trades_button")
        )
    }
}
