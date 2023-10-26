package com.diffy.broke.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.diffy.broke.database.Tags
import com.diffy.broke.database.Transactions

data class TransactionWithTags(
    @Embedded val transaction: Transactions,
    @Relation(
        parentColumn = "id",
        entityColumn = "tagid",
        associateBy = Junction(TransactionWithTagsCrossRef::class)
    )
    val tags: List<Tags>
)