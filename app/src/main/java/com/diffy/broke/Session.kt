package com.diffy.broke

import com.diffy.broke.domain.model.OrderBy
import com.diffy.broke.domain.model.SortView
import com.diffy.broke.presentation.core.ui.util.getEndOfMonthInMillis
import com.diffy.broke.presentation.core.ui.util.getStartOfMonthInMillis
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Session @Inject constructor() {
    var startDate: Long = getStartOfMonthInMillis()
    var endDate: Long = getEndOfMonthInMillis()
    val orderBy: OrderBy = OrderBy.ASCENDING
    val sortView: SortView = SortView.ALL
}