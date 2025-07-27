package com.diffy.broke.core.di

import com.diffy.broke.core.AppPreferences
import com.diffy.broke.data.Databases
import com.diffy.broke.data.repository.CommonDbRepoImpl
import com.diffy.broke.domain.repository.CommonDbRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomBackupModule {

    @Provides
    fun provideCommonDbRepo( db: Databases, preferences: AppPreferences): CommonDbRepo {
        return CommonDbRepoImpl(
            db = db,
            preferences = preferences
        )
    }
}