
package com.diffy.broke.backup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.android.gms.auth.api.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveResourceClient
import com.google.android.gms.drive.MetadataChangeSet
import java.io.File


/*object GoogleDriveHelper {
    private const val TAG = "GoogleDriveHelper"
    private const val MIME_TYPE_TEXT = "text/plain"
    private const val RC_SIGN_IN = 9001

    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    fun initializeSignInLauncher(activity: ComponentActivity) {
        signInLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // User signed in successfully, perform backup
                result.data?.getStringExtra("filePath")?.let { filePath ->
                    performBackup(activity, filePath)
                }
            } else {
                // Handle sign-in failure, show an error message or take appropriate action
                Log.e(TAG, "Google Sign-In failed")
            }
        }
    }

    fun backupToDrive(context: Context, filePath: String) {
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            // User is already signed in, perform backup
            performBackup(context, filePath)
        } else {
            // User is not signed in, initiate sign-in process
            signInLauncher.launch(createSignInIntent(context, filePath))
        }
    }

    private fun createSignInIntent(context: Context, filePath: String): Intent {
        val client: GoogleSignInClient = GoogleSignIn.getClient(context as Activity, getSignInOptions())
        return client.signInIntent.apply {
            putExtra("filePath", filePath)
        }
    }

    private fun getSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Drive.SCOPE_FILE)
            .requestEmail()
            .build()
    }

    private fun performBackup(context: Context, filePath: String) {
        // TODO: Implement the backup logic here
        // You can use the GoogleApiClient or DriveResourceClient to perform backup operations
        // Example: uploadFileToDrive(context, filePath)
    }

    private fun uploadFileToDrive(context: Context, filePath: String) {
        val client: DriveResourceClient = Drive.getDriveResourceClient(context as Activity, getSignInAccount(context)!!)
        val contentUri = FileProvider.getUriForFile(context, "com.your.package.name.fileprovider", File(filePath))

        val metadataChangeSet = MetadataChangeSet.Builder()
            .setTitle("Backup.txt") // Set your desired file name
            .setMimeType(MIME_TYPE_TEXT)
            .build()

        client.createContents().continueWithTask { task ->
            val contents = task.result
            val outputStream = contents.outputStream
            val inputStream = context.contentResolver.openInputStream(contentUri)

            try {
                inputStream?.copyTo(outputStream)
            } finally {
                inputStream?.close()
                outputStream.close()
            }

            return@continueWithTask client.createFile(metadataChangeSet, contents).asTask()
        }

    }

    private fun getSignInAccount(context: Context): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    // TODO: Add methods to download files from Drive if needed
}*/


/*
fun remoteBackup(context: Context, db: Databases, backup: RoomBackup) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(DriveScopes.DRIVE_FILE))
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val signInIntent = googleSignInClient.signInIntent
    (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)

    if (requestCode == RC_SIGN_IN) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            // You are now signed in, you can use the account to authenticate with Google Drive
            val credential = GoogleAccountCredential.usingOAuth2(
                get, listOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = account!!.account
            println(credential.selectedAccount.name)
        } catch (e: ApiException) {
            // Handle sign-in failure
        }
    }

}

fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    onActivityResult(requestCode, resultCode, data)

    if (requestCode == RC_SIGN_IN) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            // You are now signed in, you can use the account to authenticate with Google Drive
            val credential = GoogleAccountCredential.usingOAuth2(
                get, listOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = account!!.account
            println(credential.selectedAccount.name)
        } catch (e: ApiException) {
            // Handle sign-in failure
        }
    }
}*/
