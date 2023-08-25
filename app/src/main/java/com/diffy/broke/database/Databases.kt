package com.diffy.broke.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Transactions::class
    ],
    version = 1,
)
abstract class Databases: RoomDatabase() {

    abstract val dao: Dao
}
