package com.diffy.broke.backup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import java.io.File

class RemoteDemo (
    private var context: Context
) {
    private var RC_SIGN_IN = 550
    fun handleSignData(data: Intent?) {
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnCompleteListener {
                Log.d("isSuccessful", "${it.isSuccessful}")
                if (it.isSuccessful) {
                    /*viewModel.updateAccountInfo(it.result)*/
                    // user successfully logged-in
                    Log.d("account"," ${it.result?.account}")
                    Log.d("displayName"," ${it.result?.displayName}")
                    Log.d("Email"," ${it.result?.email}")
                } else {
                    // authentication failed
                    Log.e("exception"," ${it.exception}")
                    /*errorInfo = PIError(message = it.exception?.message)*/
                }
            }

    }

    fun getDriveInstance(activity: Activity): Drive? {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(activity)
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE_APPDATA))
            .build()

        if (googleSignInAccount != null) {
            val credentials = GoogleAccountCredential.usingOAuth2(activity, listOf(DriveScopes.DRIVE_FILE))
            credentials.selectedAccount = googleSignInAccount.account
            return Drive.Builder(
                NetHttpTransport(), GsonFactory.getDefaultInstance(), credentials
            ).setApplicationName("broke").build()
        } else {
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)
            val signInIntent = googleSignInClient.signInIntent

            // Start the Google Sign-In activity with traditional startActivityForResult
            activity.startActivityForResult(signInIntent, RC_SIGN_IN)

            // Note: Make sure to handle the result in the onActivityResult method of the calling activity
        }
        return null
    }


    fun createFolder(): String? {
        val googledrive = getDriveInstance(this as Activity)
        var folderid: String? = null

        if (googledrive != null) {
            try {
                val folderData = com.google.api.services.drive.model.File()
                folderData.name = "broke"
                folderData.mimeType = "application/folder"
                val folder = googledrive.Files().create(folderData).execute()
                Log.i("folderid",folder.name)
                folderid = folder.id
                return folderid
            } catch (e:Exception) {
                Log.e("Exeption",e.toString())
            }
        }
        return folderid
    }


    fun uploadFileToGDrive(
        file: File,
    ): com.google.api.services.drive.model.File? {

        val folderId = createFolder()
        getDriveInstance(this as Activity)?.let { googleDriveService ->
            try {
                val gfile = com.google.api.services.drive.model.File()
                gfile.name = file.name
                folderId?.let {
                    gfile.parents = listOf(folderId)
                }
                val fileContent = FileContent("application/sqlite3", file)
                val result = googleDriveService.Files().create(gfile, fileContent).execute()

                Log.i("File has been updated loaded successfully:","${result.id}, ${result.name}, ${result.webContentLink}, ${result.webViewLink}")
                return result
            } catch (e: Exception) {
                Log.e("error",e.toString())
                e.printStackTrace()
            }

        } ?: Log.e("Signin error"," - not logged in")
        return null
    }
}