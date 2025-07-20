package com.diffy.broke.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.diffy.broke.data.entity.AccountGroup
import com.diffy.broke.data.entity.AccountHead

data class AccountHeadWithGroup(
    @Embedded
    val accountHead: AccountHead,

    @Relation(
        parentColumn = "account_group_id",
        entityColumn = "id"
    )
    val accountGroup: AccountGroup
)