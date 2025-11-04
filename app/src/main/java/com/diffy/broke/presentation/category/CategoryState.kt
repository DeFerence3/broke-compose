package com.diffy.broke.presentation.category

import com.diffy.broke.data.entity.Category as entity
import com.diffy.broke.domain.model.Category

data class CategoryState(
    val isLoading: Boolean = false,
    val categorys: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val groups: List<Category> = emptyList(),
    val searchQry: String = ""
)