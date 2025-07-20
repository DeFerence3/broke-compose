package com.diffy.broke.domain.use_case.accountgroup

import com.diffy.broke.domain.model.AccountGroup
import com.diffy.broke.domain.repository.AccountGroupRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchAccountGroupUsecase @Inject constructor(
    private val accountGroupRepo: AccountGroupRepo
) {
    suspend operator fun invoke(accountGroupName: String): Flow<List<AccountGroup>> =
        flow {
            val groups: Flow<List<com.diffy.broke.data.entity.AccountGroup>> =  accountGroupRepo.searchAccountGroup(name = accountGroupName)
            groups.collect { list ->
                emit( list.map {
                    AccountGroup(
                        id = it.id,
                        accountGroupName = it.name,
                        accountHeads = emptyList(),
                        classification = it.classification,
                        description = it.description
                    )
                } )
            }
        }
}
