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

val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `transaction` ADD COLUMN `is_income` INTEGER NOT NULL DEFAULT 0;")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `monthly_budget` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `budget` INTEGER NOT NULL,
                `month` INTEGER NOT NULL,
                `year` INTEGER NOT NULL
            )
        """)

        // Add unique index on (month, year)
        db.execSQL("""
            CREATE UNIQUE INDEX IF NOT EXISTS `index_monthly_budget_month_year`
            ON `monthly_budget` (`month`, `year`)
        """)

        // Create table: category_budget
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `category_budget` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `budget` INTEGER NOT NULL,
                `category_id` INTEGER NOT NULL,
                `monthly_budget_id` INTEGER NOT NULL,
                FOREIGN KEY(`category_id`) REFERENCES `category`(`id`)
                    ON UPDATE NO ACTION ON DELETE NO ACTION,
                FOREIGN KEY(`monthly_budget_id`) REFERENCES `monthly_budget`(`id`)
                    ON UPDATE NO ACTION ON DELETE NO ACTION
            )
        """)

        // Add indices for foreign keys
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_category_budget_category_id`
            ON `category_budget` (`category_id`)
        """)
        db.execSQL("""
            CREATE INDEX IF NOT EXISTS `index_category_budget_monthly_budget_id`
            ON `category_budget` (`monthly_budget_id`)
        """)
    }
}
