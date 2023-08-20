package com.diffy.broke.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(

    val expTitle: String,
    val expAmnt: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
