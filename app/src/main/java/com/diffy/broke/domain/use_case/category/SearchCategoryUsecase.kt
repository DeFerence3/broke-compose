package com.diffy.broke.domain.use_case.category

import com.diffy.broke.domain.model.Category
import com.diffy.broke.domain.repository.CategoryRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchCategoryUsecase @Inject constructor(
    private val categoryRepo: CategoryRepo
) {
    suspend operator fun invoke(categoryName: String): Flow<List<Category>> =
        categoryRepo.searchCategory(name = categoryName)
            .map { categories ->
                categories.map { Category.fromEntity(it) }
            }

}