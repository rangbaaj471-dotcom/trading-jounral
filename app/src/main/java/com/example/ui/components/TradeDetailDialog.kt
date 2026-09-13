package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.TradeEntity
import com.example.ui.ApexFxViewModel
import com.example.ui.theme.ApexCardBorder
import com.example.ui.theme.ApexError
import com.example.ui.theme.ApexOnPrimary
import com.example.ui.theme.ApexOnSurface
import com.example.ui.theme.ApexOnSurfaceVariant
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexOutlineVariant
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexPrimaryContainer
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexSurfaceHigh

@Composable
fun TradeDetailDialog(
    trade: TradeEntity,
    viewModel: ApexFxViewModel,
    onDismiss: () -> Unit
) {
    var isClosingTrade by remember { mutableStateOf(false) }
    var exitPriceInput by remember { mutableStateOf(trade.takeProfit.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ApexSurfaceContainer,
        modifier = Modifier.testTag("trade_detail_dialog"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = trade.pair,
                        color = ApexOnSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                if (trade.direction.equals("BUY", ignoreCase = true)) ApexSecondary.copy(alpha = 0.2f)
                                else ApexError.copy(alpha = 0.2f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = trade.direction,
                            color = if (trade.direction.equals("BUY", ignoreCase = true)) ApexSecondary else ApexError,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = ApexOutline)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Key execution row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ApexSurfaceHigh)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "ENTRY", color = ApexOutline, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${trade.entryPrice}", color = ApexOnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(text = "STOP LOSS", color = ApexError, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${trade.stopLoss}", color = ApexError, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(text = "TAKE PROFIT", color = ApexSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${trade.takeProfit}", color = ApexSecondary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text(text = "LOTS", color = ApexOutline, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${trade.lots}L", color = ApexOnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Realized outcomes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "NET P&L", color = ApexOutline, fontSize = 11.sp)
                        Text(
                            text = "${if (trade.pnl >= 0) "+$" else "-$"}${String.format("%.2f", Math.abs(trade.pnl))}",
                            color = if (trade.pnl >= 0) ApexSecondary else ApexError,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(text = "PIPS", color = ApexOutline, fontSize = 11.sp)
                        Text(
                            text = "${if (trade.pips >= 0) "+" else ""}${String.format("%.1f", trade.pips)}p",
                            color = if (trade.pips >= 0) ApexSecondary else ApexError,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(text = "R:R REALIZED", color = ApexOutline, fontSize = 11.sp)
                        Text(
                            text = "1:${trade.riskReward}",
                            color = ApexPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Setup & Session
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Setup: ${trade.setupTag}", color = ApexOnSurface, fontSize = 12.sp)
                    Text(text = "Session: ${trade.session}", color = ApexOutline, fontSize = 12.sp)
                }

                if (trade.psychologicalNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Psychological Notes:", color = ApexOutline, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(ApexSurfaceHigh)
                            .padding(8.dp)
                    ) {
                        Text(text = trade.psychologicalNotes, color = ApexOnSurfaceVariant, fontSize = 12.sp)
                    }
                }

                // If open position, allow closing with exit price
                if (trade.status.equals("OPEN", ignoreCase = true)) {
                    Spacer(modifier = Modifier.height(12.dp))
                    if (!isClosingTrade) {
                        Button(
                            onClick = { isClosingTrade = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ApexSecondary, contentColor = Color(0xFF003824)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("close_position_button")
                        ) {
                            Text("Close Position / Realize P&L")
                        }
                    } else {
                        Column {
                            Text(text = "Exit Price:", color = ApexOutline, fontSize = 12.sp)
                            OutlinedTextField(
                                value = exitPriceInput,
                                onValueChange = { exitPriceInput = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = ApexOnSurface,
                                    unfocusedTextColor = ApexOnSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    val price = exitPriceInput.toDoubleOrNull() ?: trade.entryPrice
                                    viewModel.closeOpenTrade(trade, price)
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ApexSecondary, contentColor = Color(0xFF003824)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Confirm Close")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row {
                IconButton(
                    onClick = { viewModel.deleteTrade(trade) },
                    modifier = Modifier.testTag("delete_trade_button")
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = ApexError)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Done", color = ApexPrimaryContainer)
                }
            }
        }
    )
}
