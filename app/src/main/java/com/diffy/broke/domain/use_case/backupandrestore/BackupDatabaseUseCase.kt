package com.diffy.broke.domain.use_case.backupandrestore

import android.content.Intent
import android.util.Log
import com.diffy.broke.core.AppPreferences
import com.diffy.broke.data.Databases
import com.diffy.broke.presentation.MainActivity
import de.raphaelebner.roomdatabasebackup.core.RoomBackup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class BackupDatabaseUseCase @Inject constructor(
    @dagger.hilt.android.qualifiers.ActivityContext private val activity: MainActivity,
    private val db: Databases,
    private val preferences: AppPreferences,
) {
    operator fun invoke(): Flow<Boolean> = callbackFlow {
        val backup = RoomBackup(activity)
        backup
            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
            .database(db)
            .enableLogDebug(true)
            .customLogTag("BrokeBackup")
            .backupIsEncrypted(false)
            .maxFileCount(10)
            .apply {
                onCompleteListener { success, message, exitCode ->
                    Log.i("BrokeBackup", "Success---> $success")
                    Log.i("BrokeBackup", "Message---> $message")
                    Log.i("BrokeBackup", "ExitCode---> $exitCode")

                    launch {
                        if (success) {
                            preferences.setLastBackupDate(Date())
                        }
                        restartApp(Intent(context, MainActivity::class.java))
                        trySend(success)
                        close()
                    }
                }
            }.backup()

        awaitClose()
    }.flowOn(Dispatchers.IO)
}