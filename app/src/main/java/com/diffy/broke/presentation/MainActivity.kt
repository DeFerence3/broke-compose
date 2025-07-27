package com.diffy.broke.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.diffy.broke.core.AppPreferences
import com.diffy.broke.data.Databases
import com.diffy.broke.data.backup.RoomBackup
import com.diffy.broke.data.repository.setActivityContextProvider
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

    private val roomBackup = RoomBackup(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setActivityContextProvider{ roomBackup }
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
}