package com.diffy.broke.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.diffy.broke.core.ioThread
import com.diffy.broke.data.converter.InstantConverter
import com.diffy.broke.data.dao.CategoryDao
import com.diffy.broke.data.dao.ConfigDao
import com.diffy.broke.data.dao.TransactionDao
import com.diffy.broke.data.dao.TransactionGroupDao
import com.diffy.broke.data.entity.CategoryBudget
import com.diffy.broke.data.entity.MonthlyBudget
import com.diffy.broke.data.entity.Transaction
import com.diffy.broke.data.entity.TransactionGroup

@Database(
    entities = [
        TransactionGroup::class,
        com.diffy.broke.data.entity.Category::class,
        Transaction::class,
        com.diffy.broke.data.entity.Config::class,
        MonthlyBudget::class,
        CategoryBudget::class
    ],
    version = 4
)
@TypeConverters(InstantConverter::class)
abstract class Databases: RoomDatabase() {

    abstract val transactionDao: TransactionDao

    abstract val categoryDao: CategoryDao

    abstract val transactionGroupDao: TransactionGroupDao

    abstract val configDao: ConfigDao

    companion object {

        @Volatile
        private var INSTANCE: Databases? = null

        fun getInstance(context: Context): Databases =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context = context.applicationContext,
                klass = Databases::class.java, name = "broke.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        ioThread {
                            INSTANCE?.categoryDao?.insertAll(defaultCategory)
                        }
                    }
                })
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .build()
    }
}

