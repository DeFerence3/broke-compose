package com.diffy.broke

import android.util.Log
import com.diffy.broke.database.Dao
import com.diffy.broke.database.relations.TransactionWithTags
import com.diffy.broke.dataclasses.TransactionsInTimeperiod
import com.diffy.broke.state.SortView
import com.diffy.broke.utilcomponents.formatDateFromMilliseconds
import com.diffy.broke.utilcomponents.splitDateRange
import kotlinx.coroutines.flow.firstOrNull

suspend fun getTransactions(
    sort: SortView,
    order: Int,
    startDateInMillis: Long,
    endDateInMillis: Long,
    dao: Dao
): List<TransactionsInTimeperiod> {

    val transactionsintimeperiod = (mutableListOf <TransactionsInTimeperiod>())
    var transactionsList: List<TransactionWithTags>?
    val splittedDated = splitDateRange(startDateInMillis,endDateInMillis)
    Log.d("getTransactions", "sort: $sort")
    Log.d("getTransactions", "order: $order")
    Log.d("getTransactions", "startdate: "+ formatDateFromMilliseconds(startDateInMillis))
    Log.d("getTransactions", "enddate: "+ formatDateFromMilliseconds(endDateInMillis))
/*    if (sort == SortView.ALL) {
        splittedDated.forEach{ dates ->
            val transactionsList = dao.getAllTransactionsOnDateRange(dates.startTimeMillis, dates.endTimeMillis).firstOrNull()
            if (!transactionsList.isNullOrEmpty()) {
                transactionsintimeperiod.add(
                    TransactionsInTimeperiod(
                        dates.startTimeMillis,
                        transactionsList
                    )
                )
            }
        }
    } else {
        splittedDated.forEach{ dates ->
            val transactionsList = dao.getExpenseOrIncomeOnDateRange(dates.startTimeMillis, dates.endTimeMillis,sort.ordinal).firstOrNull()
            if (!transactionsList.isNullOrEmpty()) {
                transactionsintimeperiod.add(
                    TransactionsInTimeperiod(
                        dates.startTimeMillis,
                        transactionsList
                    )
                )
            }
        }
    }*/

    splittedDated.map { dates ->
        transactionsList = if (sort == SortView.ALL) {
            dao.getTransactionsWithTagsOnDateRange(dates.startTimeMillis, dates.endTimeMillis).firstOrNull()
        } else {
            dao.getExpenseOrIncomeOnDateRange(dates.startTimeMillis, dates.endTimeMillis,sort.ordinal).firstOrNull()
        }

        if (!transactionsList.isNullOrEmpty()) {
            transactionsintimeperiod.add(
                TransactionsInTimeperiod(
                    dates.startTimeMillis,
                    transactionsList!!
                )
            )
        }
    }

    return if (order == 1 )transactionsintimeperiod.sortedBy { it.day }
    else { transactionsintimeperiod.sortedByDescending { it.day } }
}