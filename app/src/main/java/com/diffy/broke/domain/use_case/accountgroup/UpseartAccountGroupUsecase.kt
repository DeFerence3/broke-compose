package com.diffy.broke.domain.use_case.accountgroup


import com.diffy.broke.data.entity.AccountGroup
import com.diffy.broke.domain.repository.AccountGroupRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpseartAccountGroupUsecase @Inject constructor(
    private val accountGroupRepo: AccountGroupRepo
) {
    operator fun invoke(accountGroup: com.diffy.broke.domain.model.AccountGroup): Flow<Int> = flow {
        val entity = AccountGroup(
            accountGroup.id,
            accountGroup.accountGroupName,
            classification = accountGroup.classification,
            parentGroupId = accountGroup.parentGroup?.id,
            description = accountGroup.description,
        )
        emit(accountGroupRepo.upsertAccountGroup(accountGroup = entity))
    }.flowOn(Dispatchers.IO)

}