package com.diffy.broke.core.backup

import android.content.Context
import android.content.Intent
import android.util.Log
import com.diffy.broke.data.Databases
import com.diffy.broke.presentation.MainActivity
import de.raphaelebner.roomdatabasebackup.core.RoomBackup

fun backupNow(context: Context, db: Databases, backup: RoomBackup) {

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
                restartApp(Intent(context, MainActivity::class.java))
            }
        }.backup()
}

fun restoreNow(context: Context, db: Databases, backup: RoomBackup) {

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
                restartApp(Intent(context, MainActivity::class.java))
            }
        }.restore()
}