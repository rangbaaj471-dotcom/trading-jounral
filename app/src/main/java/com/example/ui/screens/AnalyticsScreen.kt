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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.ui.ApexFxViewModel
import com.example.ui.components.DrawdownChart
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

@Composable
fun AnalyticsScreen(
    viewModel: ApexFxViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTimeRange by remember { mutableStateOf("30D") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ApexSurface)
            .testTag("analytics_screen")
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Performance Analytics",
                        color = ApexOnSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Edge verification & statistical distribution",
                        color = ApexOutline,
                        fontSize = 12.sp
                    )
                }

                // Time Range Switcher
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ApexSurfaceContainer)
                        .padding(2.dp)
                ) {
                    listOf("30D", "Q3", "YTD").forEach { range ->
                        val isSelected = selectedTimeRange == range
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) ApexPrimaryContainer else Color.Transparent)
                                .clickable { selectedTimeRange = range }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("time_range_$range")
                        ) {
                            Text(
                                text = range,
                                color = if (isSelected) ApexOnPrimary else ApexOutline,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Hero Stats 2x2 Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HeroMetricCard(
                        title = "Total Trades",
                        value = "142",
                        subtitle = "+12% vs last month",
                        isPositive = true,
                        modifier = Modifier.weight(1f)
                    )
                    HeroMetricCard(
                        title = "Expectancy",
                        value = "+$214.50",
                        subtitle = "Per Trade Average",
                        isPositive = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HeroMetricCard(
                        title = "Max Drawdown",
                        value = "-4.12%",
                        subtitle = "Safe Threshold (<6%)",
                        isPositive = false,
                        badgeText = "SAFE",
                        modifier = Modifier.weight(1f)
                    )
                    HeroMetricCard(
                        title = "Sharpe Ratio",
                        value = "2.48",
                        subtitle = "Top 5% Tier",
                        isPositive = true,
                        badgeText = "ELITE",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // ApexAI Behavioral & Edge Synthesis
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ApexSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ApexAI Behavioral & Edge Synthesis",
                                color = ApexOnSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "ACTIVE MODEL",
                            color = ApexSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Synthesis Card 1: Strength
                    SynthesisInsightRow(
                        title = "PRIMARY STRENGTH",
                        description = "London Session Breakouts show 76% win rate with average 1:3.2 R:R. High discipline observed.",
                        icon = Icons.Default.WorkspacePremium,
                        accentColor = ApexSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Synthesis Card 2: Leakage
                    SynthesisInsightRow(
                        title = "LEAKAGE DETECTED",
                        description = "Counter-trend trades on Friday afternoon account for 62% of total drawdown. Recommendation: cease trading after 13:00 UTC on Fridays.",
                        icon = Icons.Default.Warning,
                        accentColor = ApexError
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Synthesis Card 3: Optimization
                    SynthesisInsightRow(
                        title = "OPTIMIZATION OPPORTUNITY",
                        description = "Trailing stops on trending pairs (USD/CAD, GBP/JPY) could improve average trade expectancy by +18%.",
                        icon = Icons.Default.Security,
                        accentColor = ApexPrimaryContainer
                    )
                }
            }
        }

        // Performance by Currency Pair
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Performance by Currency Pair",
                    color = ApexOnSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Win rate & profitability distribution across instruments",
                    color = ApexOutline,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                PairPerformanceBar(pair = "EUR/USD", winRate = 72, pnl = "+$5,420.00", trades = 38)
                Spacer(modifier = Modifier.height(8.dp))
                PairPerformanceBar(pair = "GBP/JPY", winRate = 68, pnl = "+$4,150.00", trades = 29)
                Spacer(modifier = Modifier.height(8.dp))
                PairPerformanceBar(pair = "USD/CAD", winRate = 64, pnl = "+$2,850.00", trades = 22)
                Spacer(modifier = Modifier.height(8.dp))
                PairPerformanceBar(pair = "AUD/USD", winRate = 52, pnl = "+$1,000.00", trades = 18)
            }
        }

        // Trading Sessions Breakdown
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Session Volume & Win Rates",
                        color = ApexOnSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SessionStatColumn("London", "48% Vol", "74% Win", ApexSecondary)
                        SessionStatColumn("New York", "37% Vol", "69% Win", ApexPrimaryContainer)
                        SessionStatColumn("Asian", "15% Vol", "51% Win", ApexOutline)
                    }
                }
            }
        }

        // Max Drawdown Curve
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                DrawdownChart()
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SynthesisInsightRow(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(accentColor.copy(alpha = 0.08f))
            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier
                    .size(18.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    color = accentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = ApexOnSurface,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun PairPerformanceBar(
    pair: String,
    winRate: Int,
    pnl: String,
    trades: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(ApexSurfaceContainer)
            .border(1.dp, ApexCardBorder, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = pair, color = ApexOnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "($trades trades)", color = ApexOutline, fontSize = 11.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$winRate% Win", color = ApexSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = pnl, color = ApexSecondary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { winRate / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = ApexSecondary,
                trackColor = ApexOutlineVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun SessionStatColumn(
    session: String,
    volume: String,
    winRate: String,
    accentColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = session, color = ApexOnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Text(text = volume, color = ApexOutline, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = winRate, color = accentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
