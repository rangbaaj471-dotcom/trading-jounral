package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.HeaderBar
import com.example.ui.components.TradeDetailDialog
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.NewTradeEntryScreen
import com.example.ui.screens.PlaybookScreen
import com.example.ui.screens.TradeLogScreen
import com.example.ui.theme.ApexCardBorder
import com.example.ui.theme.ApexOnPrimary
import com.example.ui.theme.ApexOnSurface
import com.example.ui.theme.ApexOutline
import com.example.ui.theme.ApexPrimary
import com.example.ui.theme.ApexPrimaryContainer
import com.example.ui.theme.ApexSecondary
import com.example.ui.theme.ApexSurface
import com.example.ui.theme.ApexSurfaceContainer
import com.example.ui.theme.ApexSurfaceLow

@Composable
fun ApexFxApp(
    viewModel: ApexFxViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val selectedTrade by viewModel.selectedTradeForDetail.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ApexSurface),
        topBar = {
            HeaderBar(
                title = when (currentTab) {
                    AppTab.DASHBOARD -> "Trading Journal"
                    AppTab.TRADE_LOG -> "Trade Log"
                    AppTab.NEW_TRADE -> "New Trade Entry"
                    AppTab.ANALYTICS -> "Analytics & Edge"
                    AppTab.PLAYBOOK -> "Strategy Playbook"
                },
                onQuickTradeClick = { viewModel.selectTab(AppTab.NEW_TRADE) },
                onNotificationsClick = { viewModel.showMessage("All risk limits and stop-loss parameters nominal.") },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = ApexSurfaceLow,
                contentColor = ApexOnSurface,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("main_navigation_bar")
            ) {
                // Dashboard Tab
                NavigationBarItem(
                    selected = currentTab == AppTab.DASHBOARD,
                    onClick = { viewModel.selectTab(AppTab.DASHBOARD) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.DASHBOARD) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                            contentDescription = "Dashboard"
                        )
                    },
                    label = { Text("Dashboard", fontSize = 10.sp, fontWeight = if (currentTab == AppTab.DASHBOARD) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ApexPrimaryContainer,
                        selectedTextColor = ApexPrimaryContainer,
                        indicatorColor = ApexSurfaceContainer,
                        unselectedIconColor = ApexOutline,
                        unselectedTextColor = ApexOutline
                    ),
                    modifier = Modifier.testTag("nav_tab_dashboard")
                )

                // Trade Log Tab
                NavigationBarItem(
                    selected = currentTab == AppTab.TRADE_LOG,
                    onClick = { viewModel.selectTab(AppTab.TRADE_LOG) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.TRADE_LOG) Icons.Filled.FormatListBulleted else Icons.Outlined.FormatListBulleted,
                            contentDescription = "Trade Log"
                        )
                    },
                    label = { Text("Trades", fontSize = 10.sp, fontWeight = if (currentTab == AppTab.TRADE_LOG) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ApexPrimaryContainer,
                        selectedTextColor = ApexPrimaryContainer,
                        indicatorColor = ApexSurfaceContainer,
                        unselectedIconColor = ApexOutline,
                        unselectedTextColor = ApexOutline
                    ),
                    modifier = Modifier.testTag("nav_tab_trade_log")
                )

                // New Trade Tab (Centered highlight)
                NavigationBarItem(
                    selected = currentTab == AppTab.NEW_TRADE,
                    onClick = { viewModel.selectTab(AppTab.NEW_TRADE) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "New Trade",
                            tint = if (currentTab == AppTab.NEW_TRADE) ApexSecondary else ApexOutline
                        )
                    },
                    label = { Text("New Trade", fontSize = 10.sp, fontWeight = if (currentTab == AppTab.NEW_TRADE) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ApexSecondary,
                        selectedTextColor = ApexSecondary,
                        indicatorColor = ApexSurfaceContainer,
                        unselectedIconColor = ApexOutline,
                        unselectedTextColor = ApexOutline
                    ),
                    modifier = Modifier.testTag("nav_tab_new_trade")
                )

                // Analytics Tab
                NavigationBarItem(
                    selected = currentTab == AppTab.ANALYTICS,
                    onClick = { viewModel.selectTab(AppTab.ANALYTICS) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.ANALYTICS) Icons.Filled.ShowChart else Icons.Outlined.ShowChart,
                            contentDescription = "Analytics"
                        )
                    },
                    label = { Text("Analytics", fontSize = 10.sp, fontWeight = if (currentTab == AppTab.ANALYTICS) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ApexPrimaryContainer,
                        selectedTextColor = ApexPrimaryContainer,
                        indicatorColor = ApexSurfaceContainer,
                        unselectedIconColor = ApexOutline,
                        unselectedTextColor = ApexOutline
                    ),
                    modifier = Modifier.testTag("nav_tab_analytics")
                )

                // Playbook Tab
                NavigationBarItem(
                    selected = currentTab == AppTab.PLAYBOOK,
                    onClick = { viewModel.selectTab(AppTab.PLAYBOOK) },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.PLAYBOOK) Icons.Filled.AutoStories else Icons.Outlined.AutoStories,
                            contentDescription = "Playbook"
                        )
                    },
                    label = { Text("Playbook", fontSize = 10.sp, fontWeight = if (currentTab == AppTab.PLAYBOOK) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ApexPrimaryContainer,
                        selectedTextColor = ApexPrimaryContainer,
                        indicatorColor = ApexSurfaceContainer,
                        unselectedIconColor = ApexOutline,
                        unselectedTextColor = ApexOutline
                    ),
                    modifier = Modifier.testTag("nav_tab_playbook")
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                AppTab.TRADE_LOG -> TradeLogScreen(viewModel = viewModel)
                AppTab.NEW_TRADE -> NewTradeEntryScreen(viewModel = viewModel)
                AppTab.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                AppTab.PLAYBOOK -> PlaybookScreen(viewModel = viewModel)
            }
        }
    }

    // Modal dialogs
    selectedTrade?.let { trade ->
        TradeDetailDialog(
            trade = trade,
            viewModel = viewModel,
            onDismiss = { viewModel.closeTradeDetail() }
        )
    }
}
