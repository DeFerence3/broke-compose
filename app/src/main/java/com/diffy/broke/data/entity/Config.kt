package com.diffy.broke.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "config")
data class Config(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "config_key")
    val configKey: String,
    @ColumnInfo(name = "config_value")
    val configValue: String,
    @ColumnInfo(name = "config_int_value")
    val configIntValue: Int?
)