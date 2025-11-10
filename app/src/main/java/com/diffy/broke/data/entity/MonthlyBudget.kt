package com.diffy.broke.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "monthly_budget",
    indices = [Index(value = ["month", "year"], unique = true)]
)
data class MonthlyBudget(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "budget")
    val budget: Int,
    @ColumnInfo(name = "month")
    val month: Int,
    @ColumnInfo(name = "year")
    val year: Int
)