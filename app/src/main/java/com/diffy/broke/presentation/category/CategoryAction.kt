package com.diffy.broke.presentation.category

import com.diffy.broke.domain.model.Category


sealed interface CategoryAction {
    data object HideAddOrEditDialog: CategoryAction
    data object AddCategory: CategoryAction
    data object SaveCategory : CategoryAction
    data class SelectCategory(val category: Category): CategoryAction
    data class SearchAccountGroup(val query: String): CategoryAction
}