package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CoreRuleEntity
import com.example.data.MindsetLogEntity
import com.example.data.StrategyPlaybookEntity
import com.example.data.TradeEntity
import com.example.data.TradeRepository
import com.example.data.TraderNotesEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppTab(val title: String) {
    DASHBOARD("Dashboard"),
    TRADE_LOG("Trade Log"),
    NEW_TRADE("New Trade"),
    ANALYTICS("Analytics"),
    PLAYBOOK("Playbook")
}

class ApexFxViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = TradeRepository(database.tradeDao(), database.playbookDao())

    // Navigation Tab
    private val _currentTab = MutableStateFlow(AppTab.DASHBOARD)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    // Trade Dialogs
    private val _selectedTradeForDetail = MutableStateFlow<TradeEntity?>(null)
    val selectedTradeForDetail: StateFlow<TradeEntity?> = _selectedTradeForDetail.asStateFlow()

    fun openTradeDetail(trade: TradeEntity) {
        _selectedTradeForDetail.value = trade
    }

    fun closeTradeDetail() {
        _selectedTradeForDetail.value = null
    }

    // Quick Trade Dialog state
    private val _showQuickTradeDialog = MutableStateFlow(false)
    val showQuickTradeDialog: StateFlow<Boolean> = _showQuickTradeDialog.asStateFlow()

    fun openQuickTradeDialog() {
        _showQuickTradeDialog.value = true
    }

    fun closeQuickTradeDialog() {
        _showQuickTradeDialog.value = false
    }

    // Status Toast / Notification
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    // Data Streams
    val allTrades: StateFlow<List<TradeEntity>> = repository.allTrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val openTrades: StateFlow<List<TradeEntity>> = repository.openTrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val closedTrades: StateFlow<List<TradeEntity>> = repository.closedTrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestMindsetLog: StateFlow<MindsetLogEntity?> = repository.latestMindsetLog
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val coreRules: StateFlow<List<CoreRuleEntity>> = repository.coreRules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val playbooks: StateFlow<List<StrategyPlaybookEntity>> = repository.playbooks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val traderNotes: StateFlow<TraderNotesEntity?> = repository.traderNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Trade Log Filters
    val searchQuery = MutableStateFlow("")
    val filterOutcome = MutableStateFlow("All") // "All", "WIN", "LOSS", "BREAK_EVEN"
    val filterSession = MutableStateFlow("All") // "All", "London", "New York", "Tokyo"

    val filteredTrades: StateFlow<List<TradeEntity>> = combine(
        allTrades,
        searchQuery,
        filterOutcome,
        filterSession
    ) { trades, query, outcome, session ->
        trades.filter { trade ->
            val matchesQuery = query.isBlank() ||
                    trade.pair.contains(query, ignoreCase = true) ||
                    trade.setupTag.contains(query, ignoreCase = true) ||
                    trade.psychologicalNotes.contains(query, ignoreCase = true)

            val matchesOutcome = when (outcome) {
                "All" -> true
                else -> trade.status.equals(outcome, ignoreCase = true)
            }

            val matchesSession = when (session) {
                "All" -> true
                else -> trade.session.equals(session, ignoreCase = true)
            }

            matchesQuery && matchesOutcome && matchesSession
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Database Actions
    fun toggleRule(rule: CoreRuleEntity) {
        viewModelScope.launch {
            repository.updateRule(rule.copy(isChecked = !rule.isChecked))
        }
    }

    fun resetRules() {
        viewModelScope.launch {
            repository.resetAllRules()
            showMessage("Core rules reset for today's trading session")
        }
    }

    fun addRule(title: String, desc: String) {
        viewModelScope.launch {
            repository.addRule(title, desc)
            showMessage("Rule added: $title")
        }
    }

    fun saveMindset(score: Float, emotion: String) {
        viewModelScope.launch {
            val dateStr = "Today, " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) + " EST"
            repository.saveMindsetLog(score, emotion, dateStr)
            showMessage("Mindset logged: Discipline score $score ($emotion)")
        }
    }

    fun saveNotes(notes: String) {
        viewModelScope.launch {
            repository.saveTraderNotes(notes)
            showMessage("Personal notes & insights saved successfully")
        }
    }

    fun deleteTrade(trade: TradeEntity) {
        viewModelScope.launch {
            repository.deleteTrade(trade.id)
            closeTradeDetail()
            showMessage("Trade ${trade.pair} deleted")
        }
    }

    fun closeOpenTrade(trade: TradeEntity, exitPrice: Double) {
        viewModelScope.launch {
            val isBuy = trade.direction.equals("BUY", ignoreCase = true)
            val pips = if (isBuy) (exitPrice - trade.entryPrice) * 10000 else (trade.entryPrice - exitPrice) * 10000
            val pnl = pips * 10 * trade.lots
            val status = if (pnl > 5) "WIN" else if (pnl < -5) "LOSS" else "BREAK_EVEN"

            val updated = trade.copy(
                exitPrice = exitPrice,
                pnl = pnl,
                pips = pips,
                status = status,
                dateDisplay = "Closed today"
            )
            repository.updateTrade(updated)
            showMessage("Closed ${trade.pair} with ${if (pnl >= 0) "+$" else "-$"}${String.format("%.2f", Math.abs(pnl))}")
        }
    }

    fun addNewTrade(
        pair: String,
        direction: String,
        lots: Double,
        entry: Double,
        sl: Double,
        tp: Double,
        session: String,
        setupTag: String,
        notes: String,
        isOpen: Boolean = false,
        exitPrice: Double? = null,
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            val isBuy = direction.equals("BUY", ignoreCase = true)
            val effectiveExit = exitPrice ?: if (isOpen) null else tp
            val pips = if (effectiveExit != null) {
                if (isBuy) (effectiveExit - entry) * 10000 else (entry - effectiveExit) * 10000
            } else 0.0

            val pnl = pips * 10 * lots
            val riskPips = Math.abs(entry - sl) * 10000
            val rewardPips = Math.abs(tp - entry) * 10000
            val rr = if (riskPips > 0) rewardPips / riskPips else 2.0

            val status = if (isOpen) "OPEN" else if (pnl > 0) "WIN" else if (pnl < 0) "LOSS" else "BREAK_EVEN"

            val dateDisplay = if (isOpen) "Active Position" else "Today, " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            val trade = TradeEntity(
                pair = pair.uppercase().trim(),
                direction = direction.uppercase().trim(),
                lots = lots,
                entryPrice = entry,
                exitPrice = effectiveExit,
                stopLoss = sl,
                takeProfit = tp,
                pnl = pnl,
                pips = pips,
                riskReward = (Math.round(rr * 10.0) / 10.0),
                session = session,
                setupTag = setupTag,
                status = status,
                psychologicalNotes = notes,
                imageUri = imageUri,
                dateDisplay = dateDisplay
            )

            repository.insertTrade(trade)
            showMessage("New trade logged: ${trade.pair} ${trade.direction} (${trade.status})")
            _currentTab.value = AppTab.TRADE_LOG
        }
    }

    fun addNewPlaybook(
        title: String,
        tag: String,
        category: String,
        winRate: Int,
        description: String,
        timeframes: String,
        step1: String,
        step2: String,
        step3: String,
        markdownDetails: String
    ) {
        viewModelScope.launch {
            val playbook = StrategyPlaybookEntity(
                title = title,
                tag = tag,
                category = category,
                winRate = winRate,
                description = description,
                timeframes = timeframes,
                step1 = step1,
                step2 = step2,
                step3 = step3,
                markdownDetails = markdownDetails,
                updatedText = "Just added"
            )
            repository.addPlaybook(playbook)
            showMessage("Playbook added: $title")
        }
    }
}
