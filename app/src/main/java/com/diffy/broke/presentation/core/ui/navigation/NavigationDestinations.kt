package com.diffy.broke.presentation.core.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.ui.graphics.vector.ImageVector
import com.diffy.broke.presentation.core.ui.Route

enum class NavigationDestinations (
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Route
) {
    TRANSACTIONS(
        Icons.Filled.Money,
        Icons.Outlined.Money,
        Route.Transaction
    ),
    SUMMARY(
        Icons.Filled.Summarize,
        Icons.Outlined.Summarize,
        Route.Summary
    ),
    CATEGORY(
        Icons.Filled.Tag,
        Icons.Outlined.Tag,
        Route.Category(isSelect = false)
    ),
    TRANSACTION_GROUP(
        Icons.Filled.Tag,
        Icons.Outlined.Tag,
        Route.TransactionGroup
    ),
    SETTINGS(
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        Route.Settings
    )
}