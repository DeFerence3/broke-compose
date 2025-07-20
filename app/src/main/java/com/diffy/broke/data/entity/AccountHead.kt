package com.diffy.broke.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "account_head",
    foreignKeys = [ForeignKey(
        entity = AccountGroup::class,
        parentColumns = ["id"],
        childColumns = ["account_group_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("account_group_id")]
)
data class AccountHead(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val accountHeadName: String,
    @ColumnInfo(name = "account_group_id")
    val accountGroupId: Int,
    @ColumnInfo(name = "opening_balance")
    val openingBalance: Double,
    @ColumnInfo(name = "balance_type")
    val balanceType: BalanceType
)