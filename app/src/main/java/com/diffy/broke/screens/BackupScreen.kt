package com.diffy.broke.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.diffy.broke.MainActivity
import com.diffy.broke.components.RestartDialog
import com.diffy.broke.database.Databases
import de.raphaelebner.roomdatabasebackup.core.RoomBackup

@Composable
fun BackupPage(db: Databases, backup: RoomBackup) {

    val context = LocalContext.current


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = paddingValues,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column (
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Backup",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                        )
                        Text(
                            text = "Last Backup: 28/08/2023 Mon, 10:10 PM \n (dummy date)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { backupNow(context, db, backup) }) {
                                Text(text = "Backup")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column (
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Restore",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                        )
                        Text(
                            text = "Last Restore: 28/08/2023 Mon, 10:10 PM \n (dummy date)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { restoreNow(context, db, backup) }) {
                                Text(text = "Restore")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun backupNow(context: Context, db: Databases, backup: RoomBackup) {

    backup
        .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
        .database(db)
        .enableLogDebug(true)
        .customLogTag("RoomBackup")
        .backupIsEncrypted(false)
        .maxFileCount(10)
        .apply {
            onCompleteListener { success, message, exitCode ->
                println( "success: $success, message: $message, exitCode: $exitCode")
                restartApp(Intent(context,MainActivity::class.java))
            }
        }.backup()
}

fun restoreNow(context: Context, db: Databases, backup: RoomBackup) {

    backup
        .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
        .database(db)
        .enableLogDebug(true)
        .customLogTag("RoomRestore")
        .backupIsEncrypted(false)
        .maxFileCount(10)
        .apply {
            onCompleteListener { success, message, exitCode ->
                println( "success: $success, message: $message, exitCode: $exitCode")
                restartApp(Intent(context,MainActivity::class.java))
            }
        }.restore()
}