package com.diffy.broke.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Upsert

@Dao
interface GenericDao<T> {
    @Upsert
    suspend fun upsertAll(entities: List<T>)

    @Insert
    fun insertAll(entities: List<T>)

    @Upsert
    suspend fun upsert(entity: T)

    @Delete
    suspend fun delete(entity: T)
}