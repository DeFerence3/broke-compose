@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.domain.use_case.transactions

import com.diffy.broke.core.BrokeResponse
import com.diffy.broke.domain.model.OrderBy
import com.diffy.broke.domain.model.SortView
import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.domain.repository.TransactionsRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class GetTransactionsUseCase @Inject constructor(
    private val transactionsRepo: TransactionsRepo
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(
        startTime: Instant,
        endTime: Instant,
        transactionsOrderBy: OrderBy,
        sortView: SortView
    ): Flow<BrokeResponse<List<Transaction>>> = transactionsRepo.getTransactionInDateRange(
        startTime = startTime,
        endTime = endTime
    ).onStart {
        BrokeResponse.Loading("")
    }.map { transactions ->
//        val times = transactions.map { it.transaction.date }.distinct()
        val timed = transactions.map { Transaction.fromEntity(it) }
        BrokeResponse.Success(timed)
    }.catch {
        BrokeResponse.Error(it.message ?: "Something went wrong")
    }
}


