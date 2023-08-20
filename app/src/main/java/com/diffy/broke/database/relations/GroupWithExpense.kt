package com.diffy.broke.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.diffy.broke.database.Expense
import com.diffy.broke.database.Group

data class GroupWithExpense (
    @Embedded val group: Group,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val expense: List<Expense>
)