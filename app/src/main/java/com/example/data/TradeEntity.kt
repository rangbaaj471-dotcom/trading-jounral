package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trades")
data class TradeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pair: String, // e.g. "EUR/USD"
    val direction: String, // "BUY" or "SELL"
    val lots: Double, // e.g. 1.50
    val entryPrice: Double,
    val exitPrice: Double?, // null if open
    val stopLoss: Double,
    val takeProfit: Double,
    val pnl: Double, // Net P&L in USD
    val pips: Double, // Pips gain/loss
    val riskReward: Double, // e.g. 2.8 for 1:2.8
    val session: String, // "London", "New York", "Tokyo"
    val setupTag: String, // e.g. "London Breakout", "Breakout Retest"
    val status: String, // "WIN", "LOSS", "OPEN", "BREAK_EVEN"
    val psychologicalNotes: String = "",
    val imageUri: String? = null,
    val dateDisplay: String, // e.g. "Today, 14:20"
    val timestamp: Long = System.currentTimeMillis()
)
