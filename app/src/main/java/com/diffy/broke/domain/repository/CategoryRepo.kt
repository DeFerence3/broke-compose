package com.diffy.broke.domain.repository

import com.diffy.broke.data.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepo {
    suspend fun upsertCategory(category: Category): Int
    suspend fun searchCategory(name: String): Flow<List<Category>>
}