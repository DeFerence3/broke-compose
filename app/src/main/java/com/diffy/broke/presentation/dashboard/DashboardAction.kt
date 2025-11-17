package com.diffy.broke.presentation.dashboard

sealed interface DashboardAction {
    data object OnNextMonth: DashboardAction
    data object OnPreviousMonth: DashboardAction
}