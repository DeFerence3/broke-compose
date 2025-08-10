package com.diffy.broke.presentation.core.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.diffy.broke.presentation.core.ui.util.scaffoldContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    list: List<SearchResult>,
    qry: String,
    onEvent: (SearchEvent) -> Unit,
    onSelect: (SearchResult) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Search") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .scaffoldContent(paddingValues)
        ) {
            OutlinedTextField(
                value = qry,
                onValueChange = { onEvent(SearchEvent.Search(it)) },
                label = { Text("Search...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (list.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.4f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results found.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn {
                    items(list){ result ->
                        ListItem(
                            headlineContent = { Text(result.name, style = MaterialTheme.typography.bodyMedium) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(result)
                                }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}