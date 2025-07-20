package com.diffy.broke.domain.use_case.accounthead

import com.diffy.broke.data.entity.BalanceType
import com.diffy.broke.domain.model.AccountHead
import com.diffy.broke.domain.repository.AccountHeadRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpseartAccountHeadUsecase @Inject constructor(
    private val accountHeadRepo: AccountHeadRepo
) {
    operator fun invoke(accountHead: AccountHead): Flow<Int> = flow {
            val entity = com.diffy.broke.data.entity.AccountHead(
                id = accountHead.id,
                accountHeadName = accountHead.accountHeadName,
                accountGroupId = accountHead.accountGroup?.id!!,
                openingBalance = 0.0,
                balanceType = BalanceType.Credit
            )
            emit(accountHeadRepo.upsertAccountHead(entity))
        }.flowOn(Dispatchers.IO)

}