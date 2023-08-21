package com.diffy.broke.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Transactions::class
    ],
    version = 6,
    autoMigrations = [
        AutoMigration( from = 1, to = 2, spec = Databases.MigrateTo3::class ),
        AutoMigration( from = 4, to = 5 ),
    ]
)
abstract class Databases: RoomDatabase() {

    abstract val dao: Dao

    @RenameColumn(tableName = "Group", fromColumnName = "groupName", toColumnName = "groupName")
    class MigrateTo3: AutoMigrationSpec

    companion object {
        val migrate2to3 = object: Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS expense (expTitle CHAR NOT NULL, expAmnt INT NOT NULL, id INT NOT NULL PRIMARY KEY)")
                database.execSQL("CREATE TABLE IF NOT EXISTS income (incTitle CHAR NOT NULL, incAmnt INT NOT NULL, id INT NOT NULL PRIMARY KEY)")
            }
        }

        val migrate3to4 = object: Migration(3,4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS transactions (transTitle CHAR NOT NULL, transAmnt INT NOT NULL, isExp BOOLEAN NOT NULL, id INT NOT NULL PRIMARY KEY)")
            }
        }

        val migrate5to6 = object: Migration(5,6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("drop table if exists expense")
                database.execSQL("drop table if exists groups")
                database.execSQL("drop table if exists income")
            }
        }
    }
}
