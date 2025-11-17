package com.diffy.broke.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.diffy.broke.data.backup.RoomBackup
import com.diffy.broke.data.repository.setActivityContextProvider
import com.diffy.broke.presentation.core.LocalThemePreference
import com.diffy.broke.presentation.core.SlidingDrawer
import com.diffy.broke.presentation.core.theme.BrokeTheme
import com.diffy.broke.presentation.core.theme.SettingsProvider
import com.diffy.broke.presentation.core.theme.conf.ThemeType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val roomBackup = RoomBackup(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setActivityContextProvider{ roomBackup }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettingsProvider {
                val themePreferences = LocalThemePreference.current
                BrokeTheme(
                    darkTheme = themePreferences.isDarkTheme,
                    dynamicColor = themePreferences.themeType == ThemeType.System
                ) {
                    SlidingDrawer()
                }
            }
        }
    }
}