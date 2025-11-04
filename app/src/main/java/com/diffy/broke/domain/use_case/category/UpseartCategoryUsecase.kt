package com.diffy.broke.domain.use_case.category

import com.diffy.broke.domain.model.Category
import com.diffy.broke.domain.repository.CategoryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import com.diffy.broke.data.entity.Category as entity

class UpseartCategoryUsecase @Inject constructor(
    private val categoryRepo: CategoryRepo
) {
    operator fun invoke(category: Category): Flow<Int> = flow {
            val entity = entity(
                id = category.id,
                categoryName = category.categoryName,
            )
            emit(categoryRepo.upsertCategory(entity))
        }.flowOn(Dispatchers.IO)

}