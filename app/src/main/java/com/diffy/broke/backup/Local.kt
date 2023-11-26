package com.diffy.broke.backup

import android.content.Context
import android.content.Intent
import com.diffy.broke.MainActivity
import com.diffy.broke.database.Databases
import de.raphaelebner.roomdatabasebackup.core.RoomBackup

fun backupNow(context: Context, db: Databases, backup: RoomBackup) {

    backup
        .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_DIALOG)
        .database(db)
        .enableLogDebug(true)
        .customLogTag("RoomBackup")
        .backupIsEncrypted(false)
        .maxFileCount(10)
        .apply {
            onCompleteListener { _, _, _ ->
                restartApp(Intent(context, MainActivity::class.java))
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
            onCompleteListener { _, _, _ ->
                restartApp(Intent(context, MainActivity::class.java))
            }
        }.restore()
}