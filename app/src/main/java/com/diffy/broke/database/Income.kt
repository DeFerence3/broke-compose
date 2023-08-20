package com.diffy.broke.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Income(

    val incTitle: String,
    val incAmnt: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
