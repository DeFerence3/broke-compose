package com.diffy.broke.core.di

import android.content.Context
import com.diffy.broke.data.Databases
import com.diffy.broke.data.dao.AccountGroupDao
import com.diffy.broke.data.dao.AccountHeadDao
import com.diffy.broke.data.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideBrokeDao(@ApplicationContext appContext: Context): TransactionDao {
        return Databases.Companion.getInstance(appContext).transactionDao
    }

    @Provides
    @Singleton
    fun provideAccountHeadDao(@ApplicationContext appContext: Context): AccountHeadDao {
        return Databases.Companion.getInstance(appContext).accountHeadDao
    }

    @Provides
    @Singleton
    fun provideAccountGroupDao(@ApplicationContext appContext: Context): AccountGroupDao {
        return Databases.Companion.getInstance(appContext).accountGroupDao
    }

}