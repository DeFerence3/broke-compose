package com.diffy.broke.presentation.core.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diffy.broke.data.entity.Classification
import com.diffy.broke.domain.model.AccountGroup
import com.diffy.broke.domain.model.AccountHead
import com.diffy.broke.domain.use_case.accountgroup.SearchAccountGroupUsecase
import com.diffy.broke.domain.use_case.accounthead.SearchAccountHeadUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchAccountHeadUseCase: SearchAccountHeadUsecase,
    private val searchAccountGroupUseCase: SearchAccountGroupUsecase,
) : ViewModel() {
    private val _state: MutableStateFlow<List<SearchResult>> = MutableStateFlow(emptyList())
    private val _searchQry = MutableStateFlow("")
    private var clazz: KClass<*>? = null

    init {
        val searchType: String = savedStateHandle.get<String>("type") ?: throw IllegalArgumentException("Missing user ID")
        clazz = searchType.let { className ->
            try {
                Class.forName(className).kotlin
            }catch (_: ClassNotFoundException){
                Log.i("Broke", "ThisClassNotFound---> $className")
                null
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }

        Log.i("Broke", "Searching for---> $clazz")

        viewModelScope.launch {
            _searchQry
                .debounce(600)
                .distinctUntilChanged()
                .collectLatest { onSearch(it) }
        }
    }

    val state: StateFlow<List<SearchResult>> = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    val searchQry: StateFlow<String> = _searchQry
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _searchQry.value
        )

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.Search -> _searchQry.update{ event.query }
        }
    }

    private inline fun <T> fetchAndMapData(
        crossinline useCaseCall: suspend () -> Flow<List<T>>,
        crossinline mapResult: (T) -> SearchResult
    ) {
        viewModelScope.launch {
            useCaseCall().collectLatest { result ->
                updateState { result.map(mapResult) }
            }
        }
    }

    private fun onSearch(query: String) {
        when(clazz){
            AccountHead::class -> { searchAccountHead(query) }
            AccountGroup::class -> { searchAccountGroup(query) }
            Classification::class -> { searchClassification(query) }
        }
    }

    private fun searchAccountGroup(query: String){
        fetchAndMapData(
            useCaseCall = { searchAccountGroupUseCase(query) },
            mapResult = { SearchResult(it, it.accountGroupName) }
        )
    }

    private fun searchClassification(query: String){
        fetchAndMapData(
            useCaseCall = { flow { emit(Classification.entries.map { it }.filter { it.name.contains(query,true) }) } },
            mapResult = { SearchResult(it, it.name) }
        )
    }

    private fun searchAccountHead(query: String){
        fetchAndMapData(
            useCaseCall = { searchAccountHeadUseCase(query) },
            mapResult = { SearchResult(it, it.accountHeadName) }
        )
    }

    private inline fun updateState(update: (List<SearchResult>) -> List<SearchResult>) {
        _state.update { update(_state.value) }
    }
}