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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diffy.broke.presentation.accountgroup.AccountGroupScreen
import com.diffy.broke.presentation.accountgroup.AccountGroupVM
import com.diffy.broke.presentation.accounthead.AccountHeadScreen
import com.diffy.broke.presentation.accounthead.AccountHeadVM
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.ui.Route
import com.diffy.broke.presentation.settings.SettingsScreen
import com.diffy.broke.presentation.settings.SettingsVM
import com.diffy.broke.presentation.summary.SummaryScreen
import com.diffy.broke.presentation.transaction.Transaction
import com.diffy.broke.presentation.transaction.TransactionVM

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
            Transaction(
                state = state,
                onEvent = viewmodel::onEvent,
                drawerClick = onDrawerClick,
                drawerState = drawerState
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

        composable<Route.AccountHead>{
            val vm = hiltViewModel<AccountHeadVM>()
            val state by vm.state.collectAsState()
            AccountHeadScreen(
                onEvent = vm::onEvent,
                state = state,
                oneTimeEventChannelFlow = vm.oneTimeEventChannelFlow,
                drawerClick = onDrawerClick,
                drawerState = drawerState
            )
        }

        composable<Route.AccountGroup>{
            val vm = hiltViewModel<AccountGroupVM>()
            val state by vm.state.collectAsState()
            AccountGroupScreen(
                onEvent = vm::onEvent,
                state = state,
                oneTimeEventChannelFlow = vm.oneTimeEventChannelFlow,
                drawerClick = onDrawerClick,
                drawerState = drawerState
            )
        }
    }
}