package com.diffy.broke.domain.repository

import com.diffy.broke.data.entity.Category
import com.diffy.broke.domain.model.CategorySpendM
import kotlinx.coroutines.flow.Flow

interface CategoryRepo {
    suspend fun upsertCategory(category: Category): Int
    suspend fun searchCategory(name: String): Flow<List<Category>>
    suspend fun getCategorySpend(month: Int,year: Int): Flow<List<CategorySpendM>>
}