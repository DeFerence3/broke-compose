package com.diffy.broke.presentation.core.search

sealed interface SearchEvent {
    data class Search(val query: String) : SearchEvent
}