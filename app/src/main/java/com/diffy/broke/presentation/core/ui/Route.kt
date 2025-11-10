package com.diffy.broke.presentation.core.ui

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
open class Route(val title: String) {
    @Serializable
    data object Transaction: Route("Transaction")

    @Serializable
    data object Summary: Route("Summary")

    @Serializable
    data object Settings: Route("Settings")

    @Serializable
    data object Tags: Route("Tags")

    @Serializable
    data object Backup: Route("Backup")

    @Serializable
    data object Home: Route("Home")

    @Serializable
    data class Category(val isSelect: Boolean): Route("Category")

    @Serializable
    data object TransactionGroup: Route("TransactionGroup")

    @Serializable
    data object Main: Route("Main")

    @Serializable
    data class Search(
        val searchType: String?,
        val key: String,
    ): Route("Search"){
        companion object {
            fun from(savedStateHandle: SavedStateHandle) = savedStateHandle.toRoute<Search>()
        }
    }
}
