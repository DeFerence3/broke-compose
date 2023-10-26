package com.diffy.broke.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tags(
    @PrimaryKey(autoGenerate = true)
    val tagid: Int,
/*    @ColumnInfo(name = "tag", defaultValue = "tag")*/
    val tag: String
)
