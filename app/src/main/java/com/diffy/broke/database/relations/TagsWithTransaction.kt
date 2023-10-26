package com.diffy.broke.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.diffy.broke.database.Tags
import com.diffy.broke.database.Transactions

data class TagsWithTransaction(
    @Embedded val tags: Tags,
    @Relation(
        parentColumn = "tagid",
        entityColumn = "id",
        associateBy = Junction(TransactionWithTagsCrossRef::class)
    )
    val transactions: List<Transactions>
)