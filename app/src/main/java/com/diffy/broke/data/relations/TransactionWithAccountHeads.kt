package com.diffy.broke.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.diffy.broke.data.entity.AccountHead
import com.diffy.broke.data.entity.Transaction

data class TransactionWithAccountHeads(
    @Embedded
    val transaction: Transaction, 

    @Relation(
        parentColumn = "debit_account_head_id", 
        entityColumn = "id",                     
        entity = AccountHead::class              
    )
    val debitAccountHead: AccountHeadWithGroup,

    @Relation(
        parentColumn = "credit_account_head_id", 
        entityColumn = "id",                     
        entity = AccountHead::class              
    )
    val creditAccountHead: AccountHeadWithGroup
)