package com.diffy.broke.presentation.dashboard

import com.diffy.broke.core.Month
import com.diffy.broke.domain.model.CategorySpend
import com.diffy.broke.domain.model.OverView
import java.util.Date

data class DashboardState(
    val isLoading: Boolean = false,
    val categorySpendM: List<CategorySpend> = emptyList(),
    val totalSpend: Double = 0.0,
    val todaySpend: Double? = null,
    val overView: OverView? = null,
    val currentMonth: Month = Month.current,
    val currentYear: Int = Date().year + 1900
)