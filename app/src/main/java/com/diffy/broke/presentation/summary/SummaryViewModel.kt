package com.diffy.broke.presentation.summary

import androidx.lifecycle.ViewModel
import com.diffy.broke.data.dao.TransactionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val transactionDao: TransactionDao
): ViewModel() {

    private val _summaryState = MutableStateFlow(SummaryStates())

    private var _startDateInMillis = MutableStateFlow(0L)
    private var _endDateInMillis = MutableStateFlow(0L)

    /*fun getTotalSpendThisMonth(): Flow<SummaryData> {
        return transactionDao.getOverView(_startDateInMillis.value,_endDateInMillis.value)
    }

    fun getTagsByTag(text: String): Flow<List<Tags>> {
        return transactionDao.getTagsByTags("$text%")
    }

    fun getExpenseByTag(): Flow<List<TransactionByTag>> {
        return transactionDao.getExpenseByTag(_startDateInMillis.value,_endDateInMillis.value)
    }

    fun getIncomeByTag(): Flow<List<TransactionByTag>> {
        return transactionDao.getIncomeByTag(_startDateInMillis.value,_endDateInMillis.value)
    }*/
}