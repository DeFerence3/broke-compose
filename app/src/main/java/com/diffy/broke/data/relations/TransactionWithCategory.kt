package com.diffy.broke.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.diffy.broke.data.entity.Category
import com.diffy.broke.data.entity.Transaction
import com.diffy.broke.data.entity.TransactionGroup

data class TransactionWithCategory(
    @Embedded
    val transaction: Transaction,

    @Relation(
        parentColumn = "category_id",
        entityColumn = "id",                     
        entity = Category::class
    )
    val category: Category,

    @Relation(
        parentColumn = "transaction_group_id",
        entityColumn = "id",                     
        entity = TransactionGroup::class
    )
    val group: TransactionGroup?
)