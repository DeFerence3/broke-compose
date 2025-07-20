package com.diffy.broke.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_group")
data class AccountGroup(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "classification")
    val classification: Classification,
    @ColumnInfo(name = "parent_group_id")
    val parentGroupId: Int?,
    @ColumnInfo(name = "description")
    val description: String?
)