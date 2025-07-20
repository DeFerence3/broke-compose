package com.diffy.broke.presentation.summary

import com.diffy.broke.domain.model.SummaryData
import com.diffy.broke.domain.model.TransactionByTag

data class SummaryStates(
    val startDateInMillis: Long = 1,
    val endDateInMillis: Long = 1,
    val summaryData: SummaryData = SummaryData(),
    val expenseWithTags: List<TransactionByTag> = emptyList(),
    val incomeWithTags: List<TransactionByTag> = emptyList()
)