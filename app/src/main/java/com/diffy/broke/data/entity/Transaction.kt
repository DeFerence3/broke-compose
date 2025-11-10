package com.diffy.broke.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Entity(
    tableName = "transaction",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"], 
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TransactionGroup::class,
            parentColumns = ["id"],
            childColumns = ["transaction_group_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("category_id"),Index("transaction_group_id")]
)
data class Transaction @OptIn(ExperimentalTime::class) constructor(
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "amount")
    val amount: Float,
    @ColumnInfo(name = "date")
    val date: Instant,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "is_income")
    val isIncome: Boolean,
    @ColumnInfo(name = "transaction_group_id")
    val transactionGroupId: Int?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
)