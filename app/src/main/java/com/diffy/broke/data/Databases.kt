package com.diffy.broke.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.diffy.broke.core.ioThread
import com.diffy.broke.data.converter.InstantConverter
import com.diffy.broke.data.dao.AccountGroupDao
import com.diffy.broke.data.dao.AccountHeadDao
import com.diffy.broke.data.dao.ConfigDao
import com.diffy.broke.data.dao.TransactionDao
import com.diffy.broke.data.entity.AccountGroup
import com.diffy.broke.data.entity.Transaction

@Database(
    entities = [
        AccountGroup::class,
        com.diffy.broke.data.entity.AccountHead::class,
        Transaction::class,
        com.diffy.broke.data.entity.Config::class
    ],
    version = 1
)
@TypeConverters(InstantConverter::class)
abstract class Databases: RoomDatabase() {

    abstract val transactionDao: TransactionDao

    abstract val accountHeadDao: AccountHeadDao

    abstract val accountGroupDao: AccountGroupDao

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
                            INSTANCE?.accountGroupDao?.insertAll(defaultAccountGroups)
                            INSTANCE?.accountHeadDao?.insertAll(defaultAccountHeads)
                        }
                    }
                })
                .build()
    }
}

