package com.diffy.broke.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.diffy.broke.core.AppPreferences
import com.diffy.broke.data.Databases
import com.diffy.broke.domain.use_case.backupandrestore.BackupDatabaseUseCase
import com.diffy.broke.domain.use_case.backupandrestore.RestoreDatabaseUseCase
import com.diffy.broke.presentation.core.LocalThemePreference
import com.diffy.broke.presentation.core.ui.theme.BrokeTheme
import com.diffy.broke.presentation.core.ui.theme.SettingsProvider
import com.diffy.broke.presentation.home.SlidingDrawer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var db: Databases

    @Inject
    lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettingsProvider {
                BrokeTheme(
                    darkTheme = LocalThemePreference.current.isDarkTheme,
                    dynamicColor = true
                ) {
                    SlidingDrawer()
                }
            }
        }
    }

    fun backup(){
        BackupDatabaseUseCase(
            db = db,
            preferences = preferences,
            activity = this
        )
    }

    fun restore(){
        RestoreDatabaseUseCase(
            activity = this,
            db = db,
            preferences = preferences,
        )
    }
}