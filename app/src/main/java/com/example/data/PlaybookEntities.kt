package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mindset_logs")
data class MindsetLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val disciplineScore: Float, // 1.0 to 10.0
    val emotionalState: String, // "Calm", "Focused", "Anxious", "FOMO Prone"
    val timestamp: Long = System.currentTimeMillis(),
    val dateDisplay: String
)

@Entity(tableName = "core_rules")
data class CoreRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val isChecked: Boolean = false
)

@Entity(tableName = "strategy_playbooks")
data class StrategyPlaybookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val tag: String, // "London Killzone", "US Session", "Swing Trading"
    val category: String, // "all", "fx", "indices"
    val winRate: Int, // e.g. 68
    val description: String,
    val timeframes: String, // "M15 / M5"
    val step1: String,
    val step2: String,
    val step3: String,
    val markdownDetails: String,
    val updatedText: String
)

@Entity(tableName = "trader_notes")
data class TraderNotesEntity(
    @PrimaryKey val id: Int = 1,
    val content: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
