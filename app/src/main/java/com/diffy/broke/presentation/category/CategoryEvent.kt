package com.diffy.broke.presentation.category

sealed interface CategoryEvent {
    data class Success(val message: String) : CategoryEvent
    data class Error(val message: String) : CategoryEvent
}