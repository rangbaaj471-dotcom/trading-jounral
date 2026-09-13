package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Query("SELECT * FROM trades ORDER BY timestamp DESC")
    fun getAllTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE status = 'OPEN' ORDER BY timestamp DESC")
    fun getOpenTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE status != 'OPEN' ORDER BY timestamp DESC")
    fun getClosedTrades(): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades WHERE id = :id LIMIT 1")
    suspend fun getTradeById(id: Long): TradeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: TradeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrades(trades: List<TradeEntity>)

    @Update
    suspend fun updateTrade(trade: TradeEntity)

    @Query("DELETE FROM trades WHERE id = :id")
    suspend fun deleteTradeById(id: Long)

    @Query("DELETE FROM trades")
    suspend fun clearAllTrades()
}
