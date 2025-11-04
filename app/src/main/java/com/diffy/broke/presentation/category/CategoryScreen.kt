package com.diffy.broke.presentation.category

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.diffy.broke.R
import com.diffy.broke.domain.model.Category
import com.diffy.broke.presentation.core.LocalNavController
import com.diffy.broke.presentation.core.search.navigateBackWithResult
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.templates.ScaffoldTemplate
import com.diffy.broke.presentation.core.ui.components.BrokeDialog
import com.diffy.broke.presentation.core.ui.util.ObserveEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onEvent: (CategoryAction) -> Unit,
    state: CategoryState,
    eventsChannel: Flow<CategoryEvent>,
    drawerClick: (SlidingDrawerState) -> Unit,
    drawerState: SlidingDrawerState,
    isSelector: Boolean
) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    eventsChannel.ObserveEvent {
        when (it) {
            is CategoryEvent.Success -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
            is CategoryEvent.Error -> {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    if (state.selectedCategory != null) {
        BrokeDialog(
            onNegativeAction = { onEvent(CategoryAction.HideAddOrEditDialog) },
            onPositiveAction = { onEvent(CategoryAction.SaveCategory) },
            positiveButtonEnabled = state.selectedCategory.categoryName.isNotBlank(),
            title = if (state.selectedCategory.id == 0) "Add Category" else "Edit Category",
            positiveText = "Submit",
            negativeText = "Cancel"
        ) {
            OutlinedTextField(
                value = state.selectedCategory.categoryName,
                onValueChange = { newValue ->
                    val updatedCategory = state.selectedCategory.copy(categoryName = newValue)
                    onEvent(CategoryAction.SelectCategory(updatedCategory))
                },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }

    // Main screen content
    ScaffoldTemplate(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.account_Head)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            drawerClick(drawerState.opposite())
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.navigation_icon))
                    }
                }
            )
        },
        floatingActionButtonText = if (!isSelector) "Add" else null,
        floatingActionButtonAction = {
            if (!isSelector){
                onEvent(CategoryAction.AddCategory)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
                Text("Loading categories...")
            } else if (state.categorys.isEmpty()) {
                Text("No categories found. Tap 'Add' to create one.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.categorys, key = { it.id }) { head ->
                        CategoryItem(head = head) {
                            if (isSelector) {
                                navController.navigateBackWithResult(head)
                            }else {
                                onEvent(CategoryAction.SelectCategory(head))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(head: Category, onClick: (Category) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(head) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = head.categoryName,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}