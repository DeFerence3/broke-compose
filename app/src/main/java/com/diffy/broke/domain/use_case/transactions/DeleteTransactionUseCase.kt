@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.domain.use_case.transactions

import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.domain.repository.TransactionsRepo
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class DeleteTransactionUseCase @Inject constructor(
    private val transactionsRepo: TransactionsRepo
) {
    suspend operator fun invoke(transactions: Transaction) {
        transactionsRepo.deleteTransaction(transactions.toEntity())
    }
}