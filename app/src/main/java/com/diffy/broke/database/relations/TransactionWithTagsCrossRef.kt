package com.diffy.broke.database.relations

import androidx.room.Entity

@Entity(primaryKeys = ["id", "tagid"])
data class TransactionWithTagsCrossRef(
    val id: Int,
    val tagid: Int
)
