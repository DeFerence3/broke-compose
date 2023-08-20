package com.diffy.broke

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.diffy.broke.database.AllDatabase
import com.diffy.broke.screens.Navigations
import com.diffy.broke.ui.theme.BrokeTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AllDatabase::class.java,
            "pack.db"
        ).addMigrations(AllDatabase.migrate2to3,AllDatabase.migrate3to4).build().dao
    }

    private val viewmodel by viewModels<com.diffy.broke.ViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ViewModel(db) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            BrokeTheme {
                // A surface container using the 'background' color from the theme
                val state by viewmodel.state.collectAsState()
                val navController = rememberNavController()
                Navigations(navController = navController,state = state, onEvent = viewmodel::onEvent )
                /*PackScreen(state = state, onEvent = viewmodel::onEvent )*/
            }
        }
    }
}