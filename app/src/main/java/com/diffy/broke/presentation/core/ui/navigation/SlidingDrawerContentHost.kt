package com.diffy.broke.presentation.core.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.diffy.broke.domain.model.Category
import com.diffy.broke.domain.model.CategorySpend
import com.diffy.broke.presentation.category.CategoryScreen
import com.diffy.broke.presentation.category.CategoryVM
import com.diffy.broke.presentation.core.search.navigateForResult
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.ui.Route
import com.diffy.broke.presentation.dashboard.DashboardScreen
import com.diffy.broke.presentation.dashboard.DashboardVM
import com.diffy.broke.presentation.settings.SettingsScreen
import com.diffy.broke.presentation.settings.SettingsVM
import com.diffy.broke.presentation.summary.SummaryScreen
import com.diffy.broke.presentation.transaction.TransactionEvents
import com.diffy.broke.presentation.transaction.TransactionScreen
import com.diffy.broke.presentation.transaction.TransactionVM
import com.diffy.broke.presentation.transactiongroup.TransactionGroupScreen
import com.diffy.broke.presentation.transactiongroup.TransactionGroupVM
import kotlinx.coroutines.launch

@Composable
fun SlidingDrawerContentHost(
    modifier: Modifier,
    navController: NavHostController,
    drawerState: SlidingDrawerState,
    onDrawerClick: (SlidingDrawerState) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Dashboard,
        enterTransition = { enterTransition(TransitionDirection.TO_LEFT)},
        exitTransition = { exitTransition(TransitionDirection.TO_LEFT)},
        popEnterTransition = { enterTransition(TransitionDirection.TO_RIGHT)},
        popExitTransition = { exitTransition(TransitionDirection.TO_RIGHT) }
    ) {

        composable<Route.Dashboard> {
            val sampleCategories = listOf(
                CategorySpend("Food", 0.0, 0.0, Icons.Default.Restaurant, Color(0xFFFF7043), Color(0xFFFF7043)),
                CategorySpend("Transport", 0.0, 0.0, Icons.Default.DirectionsBus, Color(0xFFE8F5E9), Color(0xFF43A047)),
                CategorySpend("Shopping", 0.0, 0.0, Icons.Default.ShoppingBag, Color(0xFFE3F2FD), Color(0xFF42A5F5)),
                CategorySpend("Entertainment", 0.0, 0.0, Icons.Default.Movie, Color(0xFFFFF3E0), Color(0xFFFFA726)),
                CategorySpend("Bills", 0.0, 0.0, Icons.AutoMirrored.Filled.ReceiptLong, Color(0xFFE0F7FA), Color(0xFF26C6DA))
            )

            val viewmodel =  hiltViewModel<DashboardVM>()
            val state by viewmodel.state.collectAsState()

            DashboardScreen(
                onDrawerClick = { onDrawerClick(drawerState.opposite()) },
                onEvent = viewmodel::onAction,
                state = state
            )
        }
        composable<Route.Transaction> {
            val viewmodel =  hiltViewModel<TransactionVM>()
            val state by viewmodel.state.collectAsState()

            TransactionScreen(
                state = state,
                onEvent = viewmodel::onEvent,
                drawerClick = onDrawerClick,
                drawerState = drawerState,
                onSelectClick = {
                    viewmodel.viewModelScope.launch {
                        val res = navController.navigateForResult<Category>(Route.Category(true))
                        res?.let {
                            viewmodel.onEvent(TransactionEvents.SelectCategory(it))
                        }
                    }
                }
            )
        }

        composable<Route.Summary> {
            SummaryScreen(
                drawerClick = onDrawerClick,
                drawerState = drawerState
            )
        }

        composable<Route.Settings> {
            val vm = hiltViewModel<SettingsVM>()
            val state by vm.state.collectAsState()
            SettingsScreen(
                drawerClick = onDrawerClick,
                drawerState = drawerState,
                state = state,
                onEvent = vm::onEvent,
                eventChanel = vm.eventChanel
            )
        }
        composable<Route.Tags> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tags")
            }
        }

        composable<Route.Category>{
            val vm = hiltViewModel<CategoryVM>()
            val state by vm.state.collectAsState()
            val category = it.toRoute<Route.Category>()
            CategoryScreen(
                onEvent = vm::onEvent,
                state = state,
                eventsChannel = vm.oneTimeEventChannelFlow,
                drawerClick = onDrawerClick,
                drawerState = drawerState,
                isSelector = category.isSelect
            )
        }

        composable<Route.TransactionGroup>{
            val vm = hiltViewModel<TransactionGroupVM>()
            val state by vm.state.collectAsState()
            TransactionGroupScreen(
                onEvent = vm::onEvent,
                state = state,
                oneTimeEventChannelFlow = vm.oneTimeEventChannelFlow,
                drawerClick = onDrawerClick,
                drawerState = drawerState,
            )
        }
    }
}