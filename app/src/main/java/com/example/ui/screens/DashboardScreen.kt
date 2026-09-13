package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.TradeEntity
import com.example.ui.ApexFxViewModel
import com.example.ui.AppTab
import com.example.ui.components.ActivePositionsTicker
import com.example.ui.components.EquityCurveChart
import com.example.ui.components.HeroMetricCard
import com.example.ui.theme.ApexCardBorder
import com.example.ui.theme.ApexError
import com.example.ui.theme.ApexOnPrimary
import com.example.ui.theme.ApexOnSurface
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexOutlineVariant
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexPrimaryContainer
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurface
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexSurfaceHigh
import com.example.ui.theme.ApexSurfaceLow

@Composable
fun DashboardScreen(
    viewModel: ApexFxViewModel,
    modifier: Modifier = Modifier
) {
    val allTrades by viewModel.allTrades.collectAsStateWithLifecycle()
    val openTrades by viewModel.openTrades.collectAsStateWithLifecycle()
    val closedTrades by viewModel.closedTrades.collectAsStateWithLifecycle()

    var isCumulativeChart by remember { mutableStateOf(true) }

    // Metrics calculations
    val netPnl = closedTrades.sumOf { it.pnl }
    val winTrades = closedTrades.filter { it.status.equals("WIN", ignoreCase = true) }
    val lossTrades = closedTrades.filter { it.status.equals("LOSS", ignoreCase = true) }
    val winRate = if (closedTrades.isNotEmpty()) (winTrades.size.toFloat() / closedTrades.size) * 100f else 68.4f
    val grossProfit = winTrades.sumOf { it.pnl }
    val grossLoss = Math.abs(lossTrades.sumOf { it.pnl })
    val profitFactor = if (grossLoss > 0) grossProfit / grossLoss else 2.41

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ApexSurface)
            .testTag("dashboard_screen")
    ) {
        // Active Positions Banner
        item {
            ActivePositionsTicker(
                openTrades = openTrades,
                onManageClick = { viewModel.selectTab(AppTab.TRADE_LOG) }
            )
        }

        // Hero Metric Cards 2x2 Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeroMetricCard(
                        title = "Net Profit",
                        value = "+$${String.format("%,.2f", if (closedTrades.isEmpty()) 13420.50 else netPnl)}",
                        subtitle = "+34.2% vs last month",
                        isPositive = true,
                        icon = Icons.Default.Timeline,
                        modifier = Modifier.weight(1f)
                    )
                    HeroMetricCard(
                        title = "Win Rate",
                        value = "${String.format("%.1f", winRate)}%",
                        subtitle = "${winTrades.size} Wins / ${lossTrades.size} Losses",
                        isPositive = true,
                        progressFraction = winRate / 100f,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HeroMetricCard(
                        title = "Profit Factor",
                        value = String.format("%.2f", profitFactor),
                        subtitle = "Expected: $219.80",
                        isPositive = true,
                        badgeText = "ELITE",
                        modifier = Modifier.weight(1f)
                    )
                    HeroMetricCard(
                        title = "Avg R:R",
                        value = "1:2.8",
                        subtitle = "Realized Risk/Reward",
                        isPositive = true,
                        icon = Icons.Default.Percent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Equity Curve Chart Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Equity Curve",
                                color = ApexOnSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Monthly capital compounding",
                                color = ApexOutline,
                                fontSize = 11.sp
                            )
                        }

                        // Toggle cumulative / daily
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ApexSurfaceHigh)
                                .padding(2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isCumulativeChart) ApexPrimaryContainer else Color.Transparent)
                                    .clickable { isCumulativeChart = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Cumulative",
                                    color = if (isCumulativeChart) ApexOnPrimary else ApexOutline,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (!isCumulativeChart) ApexPrimaryContainer else Color.Transparent)
                                    .clickable { isCumulativeChart = false }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Daily",
                                    color = if (!isCumulativeChart) ApexOnPrimary else ApexOutline,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    EquityCurveChart(isCumulative = isCumulativeChart)
                }
            }
        }

        // Quick Entry Banner Callout
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ApexSurfaceContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Execute Institutional Edge",
                            color = ApexOnSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Log your next setup with automated R:R and mindset tracking.",
                            color = ApexOutline,
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = { viewModel.selectTab(AppTab.NEW_TRADE) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ApexSecondary,
                            contentColor = Color(0xFF003824)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("dashboard_log_trade_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Log Trade", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Recent Trades Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Trades",
                    color = ApexOnSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View All (${allTrades.size})",
                    color = ApexPrimaryContainer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { viewModel.selectTab(AppTab.TRADE_LOG) }
                        .testTag("view_all_trades_link")
                )
            }
        }

        // List of recent trades
        items(allTrades.take(5)) { trade ->
            TradeItemRow(
                trade = trade,
                onClick = { viewModel.openTradeDetail(trade) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TradeItemRow(
    trade: TradeEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isWin = trade.status.equals("WIN", ignoreCase = true)
    val isLoss = trade.status.equals("LOSS", ignoreCase = true)
    val isOpen = trade.status.equals("OPEN", ignoreCase = true)
    val isBuy = trade.direction.equals("BUY", ignoreCase = true)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ApexSurfaceContainer)
            .border(1.dp, ApexCardBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .testTag("trade_item_${trade.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pair & Direction
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = trade.pair,
                        color = ApexOnSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                if (isBuy) ApexSecondary.copy(alpha = 0.2f) else ApexError.copy(alpha = 0.2f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = trade.direction,
                            color = if (isBuy) ApexSecondary else ApexError,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${trade.lots}L",
                        color = ApexOutline,
                        fontSize = 11.sp
                    )
                }

                // P&L & Status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (trade.pnl >= 0) "+$${String.format("%.2f", trade.pnl)}" else "-$${String.format("%.2f", Math.abs(trade.pnl))}",
                        color = if (trade.pnl >= 0) ApexSecondary else ApexError,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                when {
                                    isOpen -> ApexPrimaryContainer.copy(alpha = 0.2f)
                                    isWin -> ApexSecondary.copy(alpha = 0.2f)
                                    isLoss -> ApexError.copy(alpha = 0.2f)
                                    else -> ApexOutline.copy(alpha = 0.2f)
                                },
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = trade.status,
                            color = when {
                                isOpen -> ApexPrimaryContainer
                                isWin -> ApexSecondary
                                isLoss -> ApexError
                                else -> ApexOutline
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sub details row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = trade.setupTag,
                        color = ApexOutline,
                        fontSize = 11.sp
                    )
                    Text(text = " • ", color = ApexOutlineVariant, fontSize = 11.sp)
                    Text(
                        text = "${trade.session} Session",
                        color = ApexOutline,
                        fontSize = 11.sp
                    )
                    Text(text = " • ", color = ApexOutlineVariant, fontSize = 11.sp)
                    Text(
                        text = "1:${trade.riskReward} R",
                        color = ApexPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = trade.dateDisplay,
                    color = ApexOutlineVariant,
                    fontSize = 10.sp
                )
            }
        }
    }
}
