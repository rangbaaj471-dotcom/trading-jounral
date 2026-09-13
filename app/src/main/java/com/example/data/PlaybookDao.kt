package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaybookDao {
    // Mindset Logs
    @Query("SELECT * FROM mindset_logs ORDER BY timestamp DESC LIMIT 1")
    fun getLatestMindsetLog(): Flow<MindsetLogEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMindsetLog(log: MindsetLogEntity): Long

    // Core Rules
    @Query("SELECT * FROM core_rules ORDER BY id ASC")
    fun getAllRules(): Flow<List<CoreRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRules(rules: List<CoreRuleEntity>)

    @Update
    suspend fun updateRule(rule: CoreRuleEntity)

    @Query("UPDATE core_rules SET isChecked = 0")
    suspend fun resetAllRules()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: CoreRuleEntity): Long

    // Strategy Playbooks
    @Query("SELECT * FROM strategy_playbooks ORDER BY id ASC")
    fun getAllPlaybooks(): Flow<List<StrategyPlaybookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaybooks(playbooks: List<StrategyPlaybookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaybook(playbook: StrategyPlaybookEntity): Long

    // Trader Notes
    @Query("SELECT * FROM trader_notes WHERE id = 1 LIMIT 1")
    fun getTraderNotes(): Flow<TraderNotesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTraderNotes(notes: TraderNotesEntity)
}
