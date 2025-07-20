package com.diffy.broke.core.di

import android.content.Context
import com.diffy.broke.core.AppPreferences
import com.diffy.broke.data.Databases
import com.diffy.broke.data.dao.AccountGroupDao
import com.diffy.broke.data.dao.AccountHeadDao
import com.diffy.broke.data.dao.TransactionDao
import com.diffy.broke.data.repository.AccountGroupRepoImpl
import com.diffy.broke.data.repository.AccountHeadRepoImpl
import com.diffy.broke.data.repository.TransactionsRepoImpl
import com.diffy.broke.domain.repository.AccountGroupRepo
import com.diffy.broke.domain.repository.AccountHeadRepo
import com.diffy.broke.domain.repository.TransactionsRepo
import com.diffy.broke.presentation.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTransactionsRepository(transactionDao: TransactionDao): TransactionsRepo {
        return TransactionsRepoImpl(transactionDao)
    }

    @Provides
    @Singleton
    fun provideAccountHeadRepository(brokeDao: AccountHeadDao): AccountHeadRepo {
        return AccountHeadRepoImpl(brokeDao)
    }

    @Provides
    @Singleton
    fun provideAccountGroupRepository(brokeDao: AccountGroupDao): AccountGroupRepo {
        return AccountGroupRepoImpl(brokeDao)
    }

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext applicationContext: Context): Databases {
        val db by lazy { Databases.Companion.getInstance(applicationContext) }
        return db
    }

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext applicationContext: Context): AppPreferences {
        return AppPreferences(applicationContext)
    }

    @Provides
    @Singleton
    fun provideActivity(): MainActivity {
        return MainActivity()
    }

}