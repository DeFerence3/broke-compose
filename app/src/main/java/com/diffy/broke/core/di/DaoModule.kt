package com.diffy.broke.core.di

import android.content.Context
import com.diffy.broke.data.Databases
import com.diffy.broke.data.dao.CategoryDao
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
    fun provideCategoryDao(@ApplicationContext appContext: Context): CategoryDao {
        return Databases.Companion.getInstance(appContext).categoryDao
    }

}