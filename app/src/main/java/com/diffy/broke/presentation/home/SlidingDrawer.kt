package com.diffy.broke.presentation.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.diffy.broke.presentation.core.LocalNavController
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawer
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.ui.navigation.NavigationDestinations
import com.diffy.broke.presentation.core.ui.navigation.SlidingDrawerContentHost

@Composable
fun SlidingDrawer() {
    val navHostController = LocalNavController.current
    val configuration = LocalWindowInfo.current
    val density = LocalDensity.current.density
    val currentRouteName = navHostController.currentBackStackEntryAsState().value?.destination?.route
    val selectedNavigationItem = remember(currentRouteName) {
        NavigationDestinations.entries.find { it.route::class.qualifiedName == currentRouteName }
            ?: NavigationDestinations.TRANSACTIONS
    }
    var drawerState by remember { mutableStateOf(SlidingDrawerState.Closed) }
    val screenWidthDp = configuration.containerSize.width.dp
    val screenWidth = remember { with(density) { screenWidthDp } }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp,
        label = "Animated Offset"
    )

    val animateEdges by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 18.dp else 0.dp,
        label = "Animated Edges"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
        label = "Animated Scale"
    )

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {

        SlidingDrawer(
            selectedNavigationItem = selectedNavigationItem,
            onNavigationItemClick = {
                drawerState = SlidingDrawerState.Closed
                navHostController.navigate(it.route) {
                    popUpTo(navHostController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            onCloseClick = {
                drawerState = SlidingDrawerState.Closed
            }
        )

        SlidingDrawerContentHost(
            modifier = Modifier
                .offset{
                    IntOffset(animatedOffset.roundToPx(), 0)
                }
                .scale(scale = animatedScale)
                .clip(RoundedCornerShape(animateEdges)),
            navController = navHostController,
            drawerState = drawerState,
            onDrawerClick = { drawerState = it },
        )
    }
}