package com.diffy.broke.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_budget",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"]
        ),
        ForeignKey(
            entity = MonthlyBudget::class,
            parentColumns = ["id"],
            childColumns = ["monthly_budget_id"]
        )
    ],
    indices = [Index("category_id"), Index("monthly_budget_id")]
)
data class CategoryBudget(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "budget")
    val budget: Int,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "monthly_budget_id")
    val monthlyBudgetId: Int,
)
