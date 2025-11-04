package com.diffy.broke.presentation.category

import androidx.lifecycle.viewModelScope
import com.diffy.broke.domain.model.Category
import com.diffy.broke.domain.use_case.category.SearchCategoryUsecase
import com.diffy.broke.domain.use_case.category.UpseartCategoryUsecase
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@dagger.hilt.android.lifecycle.HiltViewModel
class CategoryVM @javax.inject.Inject constructor(
    private val upsertCategoryUsecase: UpseartCategoryUsecase,
    private val searchCategoryUsecase: SearchCategoryUsecase
) : androidx.lifecycle.ViewModel() {
    private val _state = kotlinx.coroutines.flow.MutableStateFlow(CategoryState())
    val state: kotlinx.coroutines.flow.StateFlow<CategoryState> = _state.asStateFlow()

    private val _oneTimeEventChannelFlow = kotlinx.coroutines.channels.Channel<CategoryEvent>()
    val oneTimeEventChannelFlow = _oneTimeEventChannelFlow.receiveAsFlow()

    init {
        updateState { it.copy(isLoading = true) }
        viewModelScope.launch {
            searchCategoryUsecase("").collectLatest{ heads ->
                updateState { it.copy(categorys = heads, isLoading = false) }
            }
        }
    }

    fun onEvent(event: CategoryAction) {
        when (event) {
            CategoryAction.AddCategory -> updateState { it.copy(selectedCategory = Category.new()) }
            CategoryAction.HideAddOrEditDialog -> updateState { it.copy(selectedCategory = null) }
            is CategoryAction.SelectCategory -> updateState { it.copy(selectedCategory = event.category) }
            CategoryAction.SaveCategory -> {
                val currentSelectedAccountGroup = state.value.selectedCategory
                if (!currentSelectedAccountGroup?.categoryName.isNullOrBlank())
                viewModelScope.launch {
                    upsertCategoryUsecase(category = currentSelectedAccountGroup).collectLatest {
                        updateState { it.copy(selectedCategory = null) }
                    }
                }
            }

            is CategoryAction.SearchAccountGroup -> {

            }
        }
    }

    private inline fun updateState(update: (CategoryState) -> CategoryState) {
        _state.value = update(_state.value)
    }
}