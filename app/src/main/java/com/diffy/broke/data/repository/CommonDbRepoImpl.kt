package com.diffy.broke.data.repository

import android.content.Intent
import com.diffy.broke.core.AppPreferences
import com.diffy.broke.core.Result
import com.diffy.broke.data.Databases
import com.diffy.broke.data.backup.RoomBackup
import com.diffy.broke.domain.repository.CommonDbRepo
import com.diffy.broke.presentation.MainActivity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

private var roomBackupProvider: () -> RoomBackup? = { null }

fun setActivityContextProvider(provider: () -> RoomBackup) {
    roomBackupProvider = provider
}

class CommonDbRepoImpl @Inject constructor(
    private val db: Databases,
    private val preferences: AppPreferences,
): CommonDbRepo {

    private val backup = roomBackupProvider.invoke()

    override fun backup(): Flow<Result<String>> = callbackFlow {
        if (backup != null){
            try {
                backup.backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                    .database(db)
                    .enableLogDebug(true)
                    .customLogTag("BrokeBackup")
                    .backupIsEncrypted(false)
                    .maxFileCount(10)
                    .apply {
                        onCompleteListener { success, message, exitCode ->
                            launch {
                                if (success) {
                                    preferences.setLastBackupDate(Date())
                                    restartApp(Intent(context, MainActivity::class.java))
                                }
                                trySend(Result.success(message))
                                close()
                            }
                        }
                    }.backup()
            }catch (e: Exception){
                e.printStackTrace()
                trySend(Result.failure(e))
                close()
            }
        } else {
            trySend(Result.failure(Exception("Room Backup Instance not available")))
        }
        awaitClose()
    }

    override fun restore(): Flow<Result<String>> = callbackFlow {
        if (backup != null){
            try {
                backup
                    .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
                    .database(db)
                    .enableLogDebug(true)
                    .customLogTag("BrokeBackup")
                    .backupIsEncrypted(false)
                    .maxFileCount(10)
                    .apply {
                        onCompleteListener { success, message, exitCode ->
                            launch {
                                if (success) {
                                    preferences.setLastRestoreDate(Date())
                                    restartApp(Intent(context, MainActivity::class.java))
                                }
                                trySend(Result.success(message))
                                close()
                            }
                        }
                    }.restore()
            }catch (e: Exception){
                e.printStackTrace()
                trySend(Result.failure(e))
                close()
            }
        } else {
            trySend(Result.failure(Exception("Room Backup Instance not available")))
        }
        awaitClose()
    }
}