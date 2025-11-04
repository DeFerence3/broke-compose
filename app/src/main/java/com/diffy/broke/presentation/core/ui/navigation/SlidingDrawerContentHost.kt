package com.diffy.broke.presentation.core.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.diffy.broke.domain.model.Category
import com.diffy.broke.presentation.category.CategoryScreen
import com.diffy.broke.presentation.category.CategoryVM
import com.diffy.broke.presentation.core.search.navigateForResult
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.ui.Route
import com.diffy.broke.presentation.settings.SettingsScreen
import com.diffy.broke.presentation.settings.SettingsVM
import com.diffy.broke.presentation.summary.SummaryScreen
import com.diffy.broke.presentation.transaction.TransactionScreen
import com.diffy.broke.presentation.transaction.TransactionEvents
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
        startDestination = Route.Transaction,
        enterTransition = { enterTransition(TransitionDirection.TO_LEFT)},
        exitTransition = { exitTransition(TransitionDirection.TO_LEFT)},
        popEnterTransition = { enterTransition(TransitionDirection.TO_RIGHT)},
        popExitTransition = { exitTransition(TransitionDirection.TO_RIGHT) }
    ) {
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