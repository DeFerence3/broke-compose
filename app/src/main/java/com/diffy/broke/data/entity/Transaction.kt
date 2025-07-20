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
            entity = AccountHead::class,
            parentColumns = ["id"], 
            childColumns = ["debit_account_head_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AccountHead::class,
            parentColumns = ["id"], 
            childColumns = ["credit_account_head_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("debit_account_head_id"), Index("credit_account_head_id")] 
)
data class Transaction @OptIn(ExperimentalTime::class) constructor(
    @ColumnInfo(name = "notes")
    val notes: String,
    @ColumnInfo(name = "amount")
    val amount: Float,
    @ColumnInfo(name = "date")
    val date: Instant,
    @ColumnInfo(name = "debit_account_head_id")
    val debitAccountHeadId: Int,
    @ColumnInfo(name = "credit_account_head_id")
    val creditAccountHeadId: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0, 
)