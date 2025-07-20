package com.diffy.broke.presentation.core.ui.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.diffy.broke.BuildConfig
import com.diffy.broke.R
import com.diffy.broke.core.REPO_URL

@Composable
fun AboutDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismiss()
        },
        title = { Text(text = stringResource(R.string.about_broke)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, REPO_URL.toUri())
                            context.startActivity(intent)
                        }
                ) {
                    Icon(painter = painterResource(id = R.drawable.github_mark_white), contentDescription = stringResource(
                        R.string.logo
                    )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(R.string.deference3_broke_compose))
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.width(10.dp))
                Row (
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(stringResource(R.string.app_name))
                    Text(BuildConfig.BUILD_TYPE)
                    Text(BuildConfig.VERSION_NAME)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = stringResource(R.string.dismiss))
            }
        },
    )
}