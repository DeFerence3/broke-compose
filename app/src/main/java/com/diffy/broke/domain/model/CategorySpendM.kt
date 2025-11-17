package com.diffy.broke.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategorySpendM(
    val name: String,
    val spent: Double,
    val budget: Double
)


data class CategorySpend(
    val name: String,
    val spent: Double,
    val budget: Double,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color
)
