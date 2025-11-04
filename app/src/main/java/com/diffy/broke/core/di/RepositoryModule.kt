package com.diffy.broke.core.di

import android.content.Context
import com.diffy.broke.core.AppPreferences
import com.diffy.broke.data.Databases
import com.diffy.broke.data.dao.CategoryDao
import com.diffy.broke.data.dao.TransactionDao
import com.diffy.broke.data.repository.CategoryRepoImpl
import com.diffy.broke.data.repository.TransactionsRepoImpl
import com.diffy.broke.domain.repository.CategoryRepo
import com.diffy.broke.domain.repository.TransactionsRepo
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
    fun provideCategoryRepository(brokeDao: CategoryDao): CategoryRepo {
        return CategoryRepoImpl(brokeDao)
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

}