package com.diffy.broke.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `account_head`")
        db.execSQL("DROP TABLE IF EXISTS `account_group`")
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `transaction_group` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `category_name` TEXT NOT NULL, 
                `description` TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `category` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `category_name` TEXT NOT NULL
            )
        """.trimIndent())
        db.execSQL("ALTER TABLE `transaction` RENAME TO `transaction_old`")
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `transaction` (
                `notes` TEXT NOT NULL, 
                `amount` REAL NOT NULL, 
                `date` TEXT NOT NULL, 
                `category_id` INTEGER NOT NULL, 
                `transaction_group_id` INTEGER, 
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                FOREIGN KEY(`category_id`) REFERENCES `category`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , 
                FOREIGN KEY(`transaction_group_id`) REFERENCES `transaction_group`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
            )
        """.trimIndent())

        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_category_id` ON `transaction` (`category_id`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transaction_transaction_group_id` ON `transaction` (`transaction_group_id`)")
        db.execSQL("""
            INSERT INTO `transaction` (`notes`, `amount`, `date`, `id`, `category_id`, `transaction_group_id`)
            SELECT notes, amount, date, id, 1, NULL FROM `transaction_old`
        """.trimIndent())
        db.execSQL("DROP TABLE `transaction_old`")
    }
}