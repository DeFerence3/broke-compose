package com.diffy.broke.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import com.diffy.broke.R
import com.diffy.broke.domain.model.AccountHead

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FromAndToSelector(
    modifier: Modifier = Modifier
) {
    var fromAccount by remember { mutableStateOf<AccountHead?>(null) }
    var toAccount by remember { mutableStateOf<AccountHead?>(null) }



    val formFocusRequesters = List(4) { FocusRequester() }

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = fromAccount?.accountHeadName ?: "",
            onValueChange = { },
            modifier = Modifier
                .focusRequester(formFocusRequesters[0])
                .clickable {
//                    fromAccountSelector.launch()
                }
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = stringResource(R.string.accounthead)
                )
            },
            enabled = false,
            label = {
                Text(text = "From Account")
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .clickable {
//                    toAccountSelector.launch()
                }
                .focusRequester(formFocusRequesters[1])
                .fillMaxWidth(),
            value = toAccount?.accountHeadName ?: "",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = stringResource(R.string.accounthead)
                )
            },
            onValueChange = {},
            enabled = false,
            label = {
                Text(text = "To Account")
            }
        )
    }
}
