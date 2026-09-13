package com.example.data

import kotlinx.coroutines.flow.Flow

class TradeRepository(
    private val tradeDao: TradeDao,
    private val playbookDao: PlaybookDao
) {
    // Trades
    val allTrades: Flow<List<TradeEntity>> = tradeDao.getAllTrades()
    val openTrades: Flow<List<TradeEntity>> = tradeDao.getOpenTrades()
    val closedTrades: Flow<List<TradeEntity>> = tradeDao.getClosedTrades()

    suspend fun insertTrade(trade: TradeEntity): Long = tradeDao.insertTrade(trade)
    suspend fun updateTrade(trade: TradeEntity) = tradeDao.updateTrade(trade)
    suspend fun deleteTrade(id: Long) = tradeDao.deleteTradeById(id)
    suspend fun getTradeById(id: Long): TradeEntity? = tradeDao.getTradeById(id)

    // Playbook & Psychology
    val latestMindsetLog: Flow<MindsetLogEntity?> = playbookDao.getLatestMindsetLog()
    suspend fun saveMindsetLog(score: Float, emotion: String, dateDisplay: String): Long {
        return playbookDao.insertMindsetLog(
            MindsetLogEntity(
                disciplineScore = score,
                emotionalState = emotion,
                dateDisplay = dateDisplay
            )
        )
    }

    val coreRules: Flow<List<CoreRuleEntity>> = playbookDao.getAllRules()
    suspend fun updateRule(rule: CoreRuleEntity) = playbookDao.updateRule(rule)
    suspend fun resetAllRules() = playbookDao.resetAllRules()
    suspend fun addRule(title: String, desc: String) = playbookDao.insertRule(
        CoreRuleEntity(title = title, description = desc, isChecked = false)
    )

    val playbooks: Flow<List<StrategyPlaybookEntity>> = playbookDao.getAllPlaybooks()
    suspend fun addPlaybook(playbook: StrategyPlaybookEntity) = playbookDao.insertPlaybook(playbook)

    val traderNotes: Flow<TraderNotesEntity?> = playbookDao.getTraderNotes()
    suspend fun saveTraderNotes(content: String) {
        playbookDao.saveTraderNotes(TraderNotesEntity(id = 1, content = content))
    }
}
