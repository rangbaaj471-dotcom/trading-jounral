package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TradeEntity::class,
        MindsetLogEntity::class,
        CoreRuleEntity::class,
        StrategyPlaybookEntity::class,
        TraderNotesEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tradeDao(): TradeDao
    abstract fun playbookDao(): PlaybookDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "apexfx_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database)
                    }
                }
            }
        }

        suspend fun populateDatabase(database: AppDatabase) {
            val tradeDao = database.tradeDao()
            val playbookDao = database.playbookDao()

            val initialTrades = listOf(
                TradeEntity(
                    pair = "EUR/USD",
                    direction = "BUY",
                    lots = 2.00,
                    entryPrice = 1.08450,
                    exitPrice = 1.08810,
                    stopLoss = 1.08150,
                    takeProfit = 1.09350,
                    pnl = 720.00,
                    pips = 36.0,
                    riskReward = 2.8,
                    session = "London",
                    setupTag = "Breakout Retest",
                    status = "WIN",
                    psychologicalNotes = "Feeling patient after a clean retest on the 15M chart. High confluence with daily support zone.",
                    dateDisplay = "Today, 14:20",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2
                ),
                TradeEntity(
                    pair = "GBP/JPY",
                    direction = "SELL",
                    lots = 1.50,
                    entryPrice = 190.250,
                    exitPrice = 189.600,
                    stopLoss = 190.800,
                    takeProfit = 188.900,
                    pnl = 975.50,
                    pips = 65.0,
                    riskReward = 2.1,
                    session = "New York",
                    setupTag = "NY Momentum Scalp",
                    status = "WIN",
                    psychologicalNotes = "Fast execution during NY open momentum. Clean liquidity purge above Asian highs.",
                    dateDisplay = "Yesterday, 09:15",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 26
                ),
                TradeEntity(
                    pair = "AUD/USD",
                    direction = "BUY",
                    lots = 1.00,
                    entryPrice = 0.65800,
                    exitPrice = 0.65550,
                    stopLoss = 0.65550,
                    takeProfit = 0.66400,
                    pnl = -250.00,
                    pips = -25.0,
                    riskReward = 0.8,
                    session = "Tokyo",
                    setupTag = "Asian Range Fade",
                    status = "LOSS",
                    psychologicalNotes = "Price broke support level unexpectedly due to sudden dollar strength. Respected hard stop loss.",
                    dateDisplay = "Oct 24, 16:40",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 48
                ),
                TradeEntity(
                    pair = "USD/CAD",
                    direction = "SELL",
                    lots = 2.50,
                    entryPrice = 1.36800,
                    exitPrice = 1.36200,
                    stopLoss = 1.37100,
                    takeProfit = 1.35900,
                    pnl = 1500.00,
                    pips = 60.0,
                    riskReward = 3.0,
                    session = "New York",
                    setupTag = "Trend Continuation",
                    status = "WIN",
                    psychologicalNotes = "Strong trend continuation after oil inventory report. Held through minor pullback.",
                    dateDisplay = "Oct 23, 11:10",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 72
                ),
                TradeEntity(
                    pair = "EUR/GBP",
                    direction = "BUY",
                    lots = 1.50,
                    entryPrice = 0.86900,
                    exitPrice = 0.87250,
                    stopLoss = 0.86650,
                    takeProfit = 0.87600,
                    pnl = 525.00,
                    pips = 35.0,
                    riskReward = 2.2,
                    session = "London",
                    setupTag = "London Open Sweep",
                    status = "WIN",
                    psychologicalNotes = "Sweep of London low followed by rapid shift in market structure.",
                    dateDisplay = "Oct 19, 15:20",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 120
                ),
                TradeEntity(
                    pair = "USD/JPY",
                    direction = "BUY",
                    lots = 2.50,
                    entryPrice = 149.800,
                    exitPrice = 150.150,
                    stopLoss = 149.450,
                    takeProfit = 150.600,
                    pnl = 875.00,
                    pips = 35.0,
                    riskReward = 2.5,
                    session = "New York",
                    setupTag = "Breakout Retest",
                    status = "WIN",
                    psychologicalNotes = "Clean 15M breakout retest near round number psychological level 150.00.",
                    dateDisplay = "Oct 18, 08:00",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 160
                ),
                // Active positions
                TradeEntity(
                    pair = "EUR/USD",
                    direction = "BUY",
                    lots = 1.50,
                    entryPrice = 1.08240,
                    exitPrice = null,
                    stopLoss = 1.07950,
                    takeProfit = 1.09200,
                    pnl = 182.40,
                    pips = 18.2,
                    riskReward = 3.3,
                    session = "London",
                    setupTag = "London Breakout Setup",
                    status = "OPEN",
                    psychologicalNotes = "Active long position holding above VWAP.",
                    dateDisplay = "Active Position",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45
                ),
                TradeEntity(
                    pair = "GBP/JPY",
                    direction = "SELL",
                    lots = 1.00,
                    entryPrice = 191.200,
                    exitPrice = null,
                    stopLoss = 191.650,
                    takeProfit = 189.800,
                    pnl = -45.00,
                    pips = -8.4,
                    riskReward = 3.1,
                    session = "New York",
                    setupTag = "NY Momentum Scalp",
                    status = "OPEN",
                    psychologicalNotes = "Testing lower boundary of Asian range.",
                    dateDisplay = "Active Position",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 30
                )
            )
            tradeDao.insertTrades(initialTrades)

            val rules = listOf(
                CoreRuleEntity(
                    title = "Never risk more than 1% per trade",
                    description = "Hard stop loss calculated before order execution.",
                    isChecked = true
                ),
                CoreRuleEntity(
                    title = "Wait for London open liquidity sweep",
                    description = "No entries between 07:00 - 08:00 UTC unless structural break.",
                    isChecked = true
                ),
                CoreRuleEntity(
                    title = "No revenge trading after 2 losses",
                    description = "Mandatory 2-hour lockout after hitting daily max drawdown.",
                    isChecked = true
                ),
                CoreRuleEntity(
                    title = "Align with Daily/4H structural bias",
                    description = "Never counter-trend trade on lower timeframes without HTF sweep.",
                    isChecked = false
                ),
                CoreRuleEntity(
                    title = "Scale out 50% at 2R target",
                    description = "Move remaining stop to break-even immediately.",
                    isChecked = false
                )
            )
            playbookDao.insertRules(rules)

            val playbooks = listOf(
                StrategyPlaybookEntity(
                    title = "London Open Liquidity Sweep (LOLS)",
                    tag = "London Killzone",
                    category = "fx",
                    winRate = 68,
                    description = "Targeting pre-London session highs/lows for institutional order block mitigation.",
                    timeframes = "M15 / M5",
                    step1 = "1. Identify Asian range extremes (00:00 - 07:00 UTC).",
                    step2 = "2. Wait for sweep of high/low in first 30m of London open.",
                    step3 = "3. Enter on M5 Market Structure Shift (MSS) with 10 pip stop.",
                    markdownDetails = "### London Open Liquidity Sweep (LOLS)\n\nExecution Checkpoints:\n• Confirmation candle must close with body > 60% of total range.\n• Avoid trading during high-impact FOMC news releases (+/- 30 mins).\n• Max daily loss cap strictly enforced at 2%.\n• Targets: Asian opposite extreme or HTF Fair Value Gap.",
                    updatedText = "Updated 2 days ago"
                ),
                StrategyPlaybookEntity(
                    title = "NASDAQ Opening Range Breakout (ORB)",
                    tag = "US Session",
                    category = "indices",
                    winRate = 72,
                    description = "Capturing high-momentum expansions following the 09:30 EST NYSE opening bell.",
                    timeframes = "M5 / M1",
                    step1 = "1. Map first 15-minute candle high and low on NAS100.",
                    step2 = "2. Wait for volume spike and candle close outside range.",
                    step3 = "3. Target 2.5R minimum with trailing stop at 15m VWAP.",
                    markdownDetails = "### NASDAQ Opening Range Breakout (ORB)\n\nExecution Checkpoints:\n• Establish 15-minute high and low at NYSE open.\n• Wait for clean 5-minute candle close outside the range.\n• Volume must exceed the 20-period moving average.\n• Scale 50% at 2R, trail remaining at VWAP.",
                    updatedText = "Updated 5 days ago"
                ),
                StrategyPlaybookEntity(
                    title = "Daily Order Block Rebalance",
                    tag = "Swing Trading",
                    category = "fx",
                    winRate = 61,
                    description = "Higher timeframe structural re-accumulation on major Forex crosses.",
                    timeframes = "Daily / H4",
                    step1 = "1. Scan Daily timeframe for unmitigated Fair Value Gaps.",
                    step2 = "2. Wait for price retest with bullish engulfing close.",
                    step3 = "3. Scale in 3 tranches targeting weekly liquidity pools.",
                    markdownDetails = "### Daily Order Block Rebalance\n\nExecution Checkpoints:\n• Daily trend bias must align with monthly market structure.\n• Wait for unmitigated Daily FVG to be tested.\n• Enter in 3 scale tranches (30%, 35%, 35%).\n• Invalidation upon daily candle close beyond order block extreme.",
                    updatedText = "Updated 1 week ago"
                )
            )
            playbookDao.insertPlaybooks(playbooks)

            playbookDao.insertMindsetLog(
                MindsetLogEntity(
                    disciplineScore = 8.5f,
                    emotionalState = "Focused",
                    dateDisplay = "Today, 07:45 EST"
                )
            )

            playbookDao.saveTraderNotes(
                TraderNotesEntity(
                    id = 1,
                    content = "### Weekly Review - October Focus\n1. Psychological Takeaway: Maintained extreme discipline during the EUR/USD chop on Tuesday. Stepped away after first stop-loss hit instead of chasing.\n2. Market Observation: London session sweeps have been particularly clean on GBP/JPY when Asian range is under 35 pips.\n3. Action Item: Increase position size by 0.25% only when 4H structural bias and 15M sweep align perfectly."
                )
            )
        }
    }
}
