package com.diffy.broke.presentation.core.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.diffy.broke.presentation.core.ui.theme.BrokeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    private var searchKey: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
        enableEdgeToEdge()
        setContent {
            BrokeTheme {
                val vm = hiltViewModel<SearchVM>()
                val state by vm.state.collectAsState()
                val qry by vm.searchQry.collectAsState()
                Search(
                    list = state,
                    qry = qry,
                    onEvent = vm::onEvent,
                    onSelect = {
                        onSelect(it)
                    }
                )
            }
        }
    }

    private fun onSelect(item: SearchResult) {
        val intent = Intent().apply {
            putExtra("item", item.item)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun getIntentData() {
        searchKey = intent.getStringExtra("type")
    }
}