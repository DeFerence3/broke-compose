package com.diffy.broke.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transactions (

    val transTitle: String,
    val transAmnt: Int,
    val isExp: Boolean,

    @ColumnInfo(name = "day", defaultValue = "0")
    val day: Long,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
