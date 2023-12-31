package com.diffy.broke

import android.os.Bundle
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
import com.diffy.broke.screens.SummaryScreen
import com.diffy.broke.screens.MainScreen
import com.diffy.broke.ui.theme.BrokeTheme
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import java.util.concurrent.Executors

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            Databases::class.java,
            "broke.db"
        )
        .setQueryExecutor(Executors.newSingleThreadExecutor())
        .addMigrations(Databases.migrate1to2)
        .build()
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
                        MainScreen(state = state, onEvent = viewmodel::onEvent, navController,viewmodel)
                    }
                    composable("backup-page") {
                        BackupPage(db,backup)
                    }
                    composable("summary") {
                        SummaryScreen(viewmodel,state,viewmodel::onEvent)
                    }
                }
            }
        }
    }
}