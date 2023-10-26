package com.diffy.broke.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.diffy.broke.database.relations.TransactionWithTagsCrossRef

@Database(
    entities = [
        Transactions::class,
        Tags::class,
        TransactionWithTagsCrossRef::class
    ],
    version = 2,
)
abstract class Databases: RoomDatabase() {

    abstract val dao: Dao

    companion object {
        val migrate1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val query1 = "CREATE TABLE IF NOT EXISTS `Tags`(`tagid` INTEGER NOT NULL PRIMARY KEY,`tag` CHAR(255) NOT NULL);"
                val query2 = "CREATE TABLE IF NOT EXISTS `TransactionWithTagsCrossRef`(" +
                        "`id` INTEGER NOT NULL," +
                        "`tagid` INTEGER NOT NULL," +
                        "PRIMARY KEY(`id`, `tagid`));"
                database.execSQL(query1)
                database.execSQL(query2)
            }
        }
    }
}
