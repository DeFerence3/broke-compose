package com.diffy.broke.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.diffy.broke.R
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.ui.theme.conf.DarkTheme
import com.diffy.broke.presentation.core.ui.util.ObserveEvent
import com.diffy.broke.presentation.core.ui.util.format
import com.diffy.broke.presentation.settings.components.ContentedSettingItem
import com.diffy.broke.presentation.settings.components.SettingItem
import com.diffy.broke.presentation.settings.components.SettingsGroup
import com.diffy.broke.presentation.settings.components.SettingsSaver
import com.diffy.settings.ui.settings.components.ItemPosition
import kotlinx.coroutines.flow.Flow

/**
 * See [Settings Impl](https://github.com/DeFerence3/Settings.git) for more information.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    drawerClick: (SlidingDrawerState) -> Unit,
    drawerState: SlidingDrawerState,
    state: SettingsState,
    onEvent: (SettingsEvents) -> Unit,
    eventChanel: Flow<SettingsOneTimeEvents>
) {
    
    val appSettings by SettingsSaver.AppSettingsStateFlow.collectAsState()
    val isDark = appSettings.themePreference.darkMode == DarkTheme.On || (appSettings.themePreference.darkMode == DarkTheme.System && isSystemInDarkTheme())
    val settingsGroups = remember(appSettings) { SettingsGroup.getSettings(appSettings,isDark) }
    val context = LocalContext.current

    eventChanel.ObserveEvent { events: SettingsOneTimeEvents ->
        when(events){
            is SettingsOneTimeEvents.ShowToast -> Toast.makeText(context, events.message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
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
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            item {
                Text(
                    text = "Backup & Restore",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 8.dp)
                        .padding(bottom = 4.dp)
                )
                ContentedSettingItem(
                    modifier = Modifier,
                    itemPosition = ItemPosition.Top,
                    onClick = { onEvent(SettingsEvents.OnBackup) },
                    title = "Backup",
                    summary = "Last Backup: ${state.lastBackupDate?.format() ?: "None"}",
                    value = "",
                )
                ContentedSettingItem(
                    modifier = Modifier,
                    itemPosition = ItemPosition.Bottom,
                    onClick = { onEvent(SettingsEvents.OnRestore) },
                    title = "Restore",
                    summary = "Last Restore: ${state.lastRestoreDate?.format() ?: "None"}",
                    value = "",
                )
            }
            settingsGroups.forEach { settingsGroup ->
                stickyHeader {
                    Text(
                        text = settingsGroup.header,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp, vertical = 8.dp)
                            .padding(bottom = 4.dp)
                    )
                }
                itemsIndexed(settingsGroup.settingsItems) { index,item ->
                    val itemPosition = when {
                        settingsGroup.settingsItems.size == 1 -> ItemPosition.Alone
                        index == 0 -> ItemPosition.Top
                        index == settingsGroup.settingsItems.lastIndex -> ItemPosition.Bottom
                        else -> ItemPosition.Middle
                    }
                    SettingItem(
                        item = item,
                        itemPosition = itemPosition
                    )
                }
            }
        }
    }
}