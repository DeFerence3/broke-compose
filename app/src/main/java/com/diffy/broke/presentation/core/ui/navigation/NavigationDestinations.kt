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
    val key: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Route
) {
    TRANSACTIONS(
        "transactions",
        Icons.Filled.Money,
        Icons.Outlined.Money,
        Route.Transaction
    ),
    SUMMARY(
        "summary",
        Icons.Filled.Summarize,
        Icons.Outlined.Summarize,
        Route.Summary
    ),
    ACCOUNT_HEAD(
        "account_head",
        Icons.Filled.Tag,
        Icons.Outlined.Tag,
        Route.AccountHead
    ),
    ACCOUNT_GROUP(
        "account_group",
        Icons.Filled.Tag,
        Icons.Outlined.Tag,
        Route.AccountGroup
    ),
    SETTINGS(
        "settings",
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        Route.Settings
    )
}