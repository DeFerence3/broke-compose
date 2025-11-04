package com.diffy.broke.data.repository

import com.diffy.broke.data.dao.CategoryDao
import com.diffy.broke.data.entity.Category
import com.diffy.broke.domain.repository.CategoryRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepoImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepo {
    override suspend fun upsertCategory(category: Category): Int {
        categoryDao.upsert(entity = category)
        return 7
    }

    override suspend fun searchCategory(name: String): Flow<List<Category>> = flow {
        emit(categoryDao.searchCategory(name = name))
    }
}