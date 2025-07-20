package com.diffy.broke.presentation.core.slidingdrawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diffy.broke.R
import com.diffy.broke.presentation.core.ui.navigation.NavigationDestinations

@Composable
fun SlidingDrawer(
    selectedNavigationItem: NavigationDestinations,
    onNavigationItemClick: (NavigationDestinations) -> Unit,
    onCloseClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(fraction = 0.6f)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_arrow_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        NavigationDestinations.entries.forEach { navigationItem ->
            NavigationItemView(
                navigationItem = navigationItem,
                selected = navigationItem == selectedNavigationItem,
                onClick = { onNavigationItemClick(navigationItem) }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun NavigationItemView(
    navigationItem: NavigationDestinations,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = 99.dp))
            .clickable { onClick() }
            .background(
                color = if (selected) MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                else Color.Unspecified,
                shape = RoundedCornerShape(99.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (selected) navigationItem.selectedIcon else navigationItem.unselectedIcon,
            contentDescription = stringResource(R.string.navigation_item_icon),
            tint = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = navigationItem.route.title,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            lineHeight = 20.sp
        )
    }
}