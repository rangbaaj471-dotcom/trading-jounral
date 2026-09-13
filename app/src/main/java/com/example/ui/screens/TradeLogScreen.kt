package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.ApexFxViewModel
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
fun TradeLogScreen(
    viewModel: ApexFxViewModel,
    modifier: Modifier = Modifier
) {
    val trades by viewModel.filteredTrades.collectAsStateWithLifecycle()
    val allTrades by viewModel.allTrades.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedOutcome by viewModel.filterOutcome.collectAsStateWithLifecycle()
    val selectedSession by viewModel.filterSession.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    val totalWins = allTrades.count { it.status.equals("WIN", ignoreCase = true) }
    val totalLosses = allTrades.count { it.status.equals("LOSS", ignoreCase = true) }
    val totalPnL = allTrades.sumOf { it.pnl }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ApexSurface)
            .testTag("trade_log_screen")
    ) {
        // Top Header Info
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
                        text = "Trade Log",
                        color = ApexOnSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Historical execution logs & analytics",
                        color = ApexOutline,
                        fontSize = 12.sp
                    )
                }

                // Export Button
                Button(
                    onClick = {
                        viewModel.showMessage("Statement exported: ${allTrades.size} trades, Net P&L: +$${String.format("%,.2f", totalPnL)}")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ApexSurfaceContainer,
                        contentColor = ApexOnSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .border(1.dp, ApexCardBorder, RoundedCornerShape(8.dp))
                        .testTag("export_statement_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Export",
                        modifier = Modifier.size(16.dp),
                        tint = ApexOutline
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Export", fontSize = 12.sp)
                }
            }
        }

        // Summary bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ApexSurfaceContainer)
                    .border(1.dp, ApexCardBorder, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "TRADES LOGGED", color = ApexOutline, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${allTrades.size}", color = ApexOnSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "TOTAL W / L", color = ApexOutline, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = "$totalWins W / $totalLosses L", color = ApexSecondary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "NET P&L", color = ApexOutline, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${if (totalPnL >= 0) "+$" else "-$"}${String.format("%,.2f", Math.abs(totalPnL))}",
                        color = if (totalPnL >= 0) ApexSecondary else ApexError,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Search Input
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text("Search by pair, setup, notes...", color = ApexOutline, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ApexOutline,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = ApexOutline)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("trade_log_search_input"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ApexSurfaceContainer,
                        unfocusedContainerColor = ApexSurfaceContainer,
                        focusedBorderColor = ApexPrimaryContainer,
                        unfocusedBorderColor = ApexCardBorder,
                        focusedTextColor = ApexOnSurface,
                        unfocusedTextColor = ApexOnSurface
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                )
            }
        }

        // Filter Chips Row
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                // Outcome Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "WIN", "LOSS", "OPEN").forEach { outcome ->
                        val isSelected = selectedOutcome == outcome
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) ApexPrimaryContainer else ApexSurfaceContainer)
                                .border(1.dp, if (isSelected) ApexPrimaryContainer else ApexCardBorder, RoundedCornerShape(20.dp))
                                .clickable { viewModel.filterOutcome.value = outcome }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("filter_outcome_$outcome")
                        ) {
                            Text(
                                text = when (outcome) {
                                    "All" -> "All Outcomes"
                                    "WIN" -> "Wins Only"
                                    "LOSS" -> "Losses Only"
                                    "OPEN" -> "Active Positions"
                                    else -> outcome
                                },
                                color = if (isSelected) ApexOnPrimary else ApexOutline,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Session Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "London", "New York", "Tokyo").forEach { session ->
                        val isSelected = selectedSession == session
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) ApexSecondary.copy(alpha = 0.2f) else ApexSurfaceContainer)
                                .border(1.dp, if (isSelected) ApexSecondary else ApexCardBorder, RoundedCornerShape(20.dp))
                                .clickable { viewModel.filterSession.value = session }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("filter_session_$session")
                        ) {
                            Text(
                                text = if (session == "All") "All Sessions" else "$session Session",
                                color = if (isSelected) ApexSecondary else ApexOutline,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Empty State
        if (trades.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = ApexOutline,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "No trades match the current filters", color = ApexOutline, fontSize = 14.sp)
                    }
                }
            }
        }

        // List of Trades
        items(trades) { trade ->
            TradeItemRow(
                trade = trade,
                onClick = { viewModel.openTradeDetail(trade) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
