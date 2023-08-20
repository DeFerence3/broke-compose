package com.diffy.broke.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(

    val groupName: String,
    val totalExp: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
