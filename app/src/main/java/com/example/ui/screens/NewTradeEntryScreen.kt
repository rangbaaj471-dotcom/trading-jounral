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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun NewTradeEntryScreen(
    viewModel: ApexFxViewModel,
    modifier: Modifier = Modifier
) {
    var pair by remember { mutableStateOf("EUR/USD") }
    var direction by remember { mutableStateOf("BUY") }
    var lots by remember { mutableStateOf("1.50") }
    var session by remember { mutableStateOf("London") }
    var entryPrice by remember { mutableStateOf("1.08450") }
    var stopLoss by remember { mutableStateOf("1.08150") }
    var takeProfit by remember { mutableStateOf("1.09350") }
    var setupTag by remember { mutableStateOf("Breakout Retest") }
    var notes by remember { mutableStateOf("High confluence setup holding above key 15M support level.") }
    var isOpenPosition by remember { mutableStateOf(false) }

    // Calculated metrics
    val entryVal = entryPrice.toDoubleOrNull() ?: 0.0
    val slVal = stopLoss.toDoubleOrNull() ?: 0.0
    val tpVal = takeProfit.toDoubleOrNull() ?: 0.0
    val lotsVal = lots.toDoubleOrNull() ?: 1.0

    val isBuy = direction == "BUY"
    val riskPips = if (entryVal > 0 && slVal > 0) Math.abs(entryVal - slVal) * 10000 else 0.0
    val rewardPips = if (entryVal > 0 && tpVal > 0) Math.abs(tpVal - entryVal) * 10000 else 0.0
    val riskRewardRatio = if (riskPips > 0) rewardPips / riskPips else 0.0
    val riskDollar = riskPips * 10 * lotsVal
    val rewardDollar = rewardPips * 10 * lotsVal

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ApexSurface)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("new_trade_entry_screen")
    ) {
        // Title Header
        Text(
            text = "New Trade Entry",
            color = ApexOnSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Log your execution parameters and psychological state",
            color = ApexOutline,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // SECTION 1: Direction Toggle (BUY vs SELL)
        Text(
            text = "TRADE DIRECTION",
            color = ApexOutline,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // BUY button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isBuy) ApexSecondary else ApexSurfaceContainer)
                    .border(
                        1.5.dp,
                        if (isBuy) ApexSecondary else ApexCardBorder,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { direction = "BUY" }
                    .testTag("direction_buy_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isBuy) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF003824),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = "BUY / LONG",
                        color = if (isBuy) Color(0xFF003824) else ApexOutline,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // SELL button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!isBuy) ApexError else ApexSurfaceContainer)
                    .border(
                        1.5.dp,
                        if (!isBuy) ApexError else ApexCardBorder,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { direction = "SELL" }
                    .testTag("direction_sell_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isBuy) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF5C0008),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = "SELL / SHORT",
                        color = if (!isBuy) Color(0xFF5C0008) else ApexOutline,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 2: Instrument & Position Size
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1.2f)) {
                Text(
                    text = "PAIR / INSTRUMENT",
                    color = ApexOutline,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = pair,
                    onValueChange = { pair = it.uppercase() },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ApexSurfaceContainer,
                        unfocusedContainerColor = ApexSurfaceContainer,
                        focusedBorderColor = ApexPrimaryContainer,
                        unfocusedBorderColor = ApexCardBorder,
                        focusedTextColor = ApexOnSurface,
                        unfocusedTextColor = ApexOnSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("trade_pair_input")
                )
            }

            Column(modifier = Modifier.weight(0.8f)) {
                Text(
                    text = "LOT SIZE",
                    color = ApexOutline,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = lots,
                    onValueChange = { lots = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ApexSurfaceContainer,
                        unfocusedContainerColor = ApexSurfaceContainer,
                        focusedBorderColor = ApexPrimaryContainer,
                        unfocusedBorderColor = ApexCardBorder,
                        focusedTextColor = ApexOnSurface,
                        unfocusedTextColor = ApexOnSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("trade_lots_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Popular pairs quick select
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("EUR/USD", "GBP/JPY", "USD/CAD", "AUD/USD", "USD/JPY", "XAU/USD", "NAS100").forEach { p ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (pair == p) ApexPrimaryContainer.copy(alpha = 0.25f) else ApexSurfaceContainer)
                        .border(1.dp, if (pair == p) ApexPrimaryContainer else ApexCardBorder, RoundedCornerShape(6.dp))
                        .clickable { pair = p }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = p,
                        color = if (pair == p) ApexPrimary else ApexOutline,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 3: Trading Session
        Text(
            text = "TRADING SESSION",
            color = ApexOutline,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("London", "New York", "Tokyo", "Sydney").forEach { s ->
                val isSelected = session == s
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) ApexPrimaryContainer else ApexSurfaceContainer)
                        .border(1.dp, if (isSelected) ApexPrimaryContainer else ApexCardBorder, RoundedCornerShape(8.dp))
                        .clickable { session = s }
                        .padding(vertical = 8.dp)
                        .testTag("session_select_$s"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = s,
                        color = if (isSelected) ApexOnPrimary else ApexOutline,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 4: Price Levels (Entry, SL, TP)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "ENTRY PRICE", color = ApexOutline, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = entryPrice,
                    onValueChange = { entryPrice = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ApexSurfaceContainer,
                        unfocusedContainerColor = ApexSurfaceContainer,
                        focusedBorderColor = ApexPrimaryContainer,
                        unfocusedBorderColor = ApexCardBorder,
                        focusedTextColor = ApexOnSurface,
                        unfocusedTextColor = ApexOnSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("entry_price_input")
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "STOP LOSS", color = ApexError, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = stopLoss,
                    onValueChange = { stopLoss = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ApexSurfaceContainer,
                        unfocusedContainerColor = ApexSurfaceContainer,
                        focusedBorderColor = ApexError,
                        unfocusedBorderColor = ApexCardBorder,
                        focusedTextColor = ApexOnSurface,
                        unfocusedTextColor = ApexOnSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stop_loss_input")
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "TAKE PROFIT", color = ApexSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = takeProfit,
                    onValueChange = { takeProfit = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ApexSurfaceContainer,
                        unfocusedContainerColor = ApexSurfaceContainer,
                        focusedBorderColor = ApexSecondary,
                        unfocusedBorderColor = ApexCardBorder,
                        focusedTextColor = ApexOnSurface,
                        unfocusedTextColor = ApexOnSurface
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("take_profit_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dynamic Risk / Reward Preview Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(ApexSurfaceContainer)
                .border(1.dp, ApexCardBorder, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "ESTIMATED RISK", color = ApexError, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "-$${String.format("%.2f", riskDollar)} (${String.format("%.1f", riskPips)}p)",
                        color = ApexError,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "R:R RATIO", color = ApexOutline, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "1:${String.format("%.1f", riskRewardRatio)}",
                        color = ApexPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "ESTIMATED REWARD", color = ApexSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "+$${String.format("%.2f", rewardDollar)} (${String.format("%.1f", rewardPips)}p)",
                        color = ApexSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 5: Setup Tag
        Text(
            text = "SETUP STRATEGY MODEL",
            color = ApexOutline,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = setupTag,
            onValueChange = { setupTag = it },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ApexSurfaceContainer,
                unfocusedContainerColor = ApexSurfaceContainer,
                focusedBorderColor = ApexPrimaryContainer,
                unfocusedBorderColor = ApexCardBorder,
                focusedTextColor = ApexOnSurface,
                unfocusedTextColor = ApexOnSurface
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("setup_tag_input")
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Setup tags chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Breakout Retest", "London Open Sweep", "NY Momentum Scalp", "Order Block Rejection", "Trend Continuation").forEach { tag ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (setupTag == tag) ApexSecondary.copy(alpha = 0.2f) else ApexSurfaceContainer)
                        .border(1.dp, if (setupTag == tag) ApexSecondary else ApexCardBorder, RoundedCornerShape(6.dp))
                        .clickable { setupTag = tag }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tag,
                        color = if (setupTag == tag) ApexSecondary else ApexOutline,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active Position Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(ApexSurfaceContainer)
                .border(1.dp, ApexCardBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Keep Position Open",
                    color = ApexOnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (isOpenPosition) "Trade will appear in active positions ticker" else "Log as closed trade with realized P&L",
                    color = ApexOutline,
                    fontSize = 11.sp
                )
            }
            Switch(
                checked = isOpenPosition,
                onCheckedChange = { isOpenPosition = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ApexSecondary,
                    checkedTrackColor = ApexSecondary.copy(alpha = 0.4f)
                ),
                modifier = Modifier.testTag("open_position_switch")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SECTION 6: Psychological & Mindset Notes
        Text(
            text = "EXECUTION MINDSET & PSYCHOLOGICAL NOTES",
            color = ApexOutline,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            minLines = 3,
            maxLines = 5,
            placeholder = { Text("Record discipline state, patience, FOMO management...", color = ApexOutline, fontSize = 12.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ApexSurfaceContainer,
                unfocusedContainerColor = ApexSurfaceContainer,
                focusedBorderColor = ApexPrimaryContainer,
                unfocusedBorderColor = ApexCardBorder,
                focusedTextColor = ApexOnSurface,
                unfocusedTextColor = ApexOnSurface
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("trade_notes_input")
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Save Button
        Button(
            onClick = {
                viewModel.addNewTrade(
                    pair = pair,
                    direction = direction,
                    lots = lotsVal,
                    entry = entryVal,
                    sl = slVal,
                    tp = tpVal,
                    session = session,
                    setupTag = setupTag,
                    notes = notes,
                    isOpen = isOpenPosition
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = ApexSecondary,
                contentColor = Color(0xFF003824)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("save_trade_button")
        ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Save Trade Entry", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
