package com.diffy.broke

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.diffy.broke.database.Databases
import com.diffy.broke.screens.BackupPage
import com.diffy.broke.screens.TransactionsScreen
import com.diffy.broke.ui.theme.BrokeTheme
import de.raphaelebner.roomdatabasebackup.core.RoomBackup

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            Databases::class.java,
            "broke.db"
        ).build()
    }

    private val viewmodel by viewModels<com.diffy.broke.ViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val backup = RoomBackup(this)
        setContent {
            BrokeTheme {
                val state by viewmodel.state.collectAsState()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "transactions-screen") {
                    composable("transactions-screen" ) {
                        TransactionsScreen(state = state, onEvent = viewmodel::onEvent, navController)
                    }
                    composable("backup-page") {
                        BackupPage(db,backup)
                    }
                }
            }
        }
    }
}