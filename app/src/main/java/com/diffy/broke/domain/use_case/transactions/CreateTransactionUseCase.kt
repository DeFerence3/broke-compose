@file:OptIn(ExperimentalTime::class)

package com.diffy.broke.domain.use_case.transactions

import com.diffy.broke.domain.model.Transaction
import com.diffy.broke.domain.repository.TransactionsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class CreateTransactionUseCase @Inject constructor(
    private val transactionsRepo: TransactionsRepo
){
    operator fun invoke(transaction: Transaction): Flow<Boolean> = flow{
        emit(transactionsRepo.createTransaction(transaction.toEntity()))
    }.flowOn(Dispatchers.IO)
}