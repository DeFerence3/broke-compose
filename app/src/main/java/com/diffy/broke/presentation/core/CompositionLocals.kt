package com.diffy.broke.presentation.core

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.diffy.broke.presentation.core.slidingdrawer.SlidingDrawerState
import com.diffy.broke.presentation.core.ui.theme.ThemePreference

val LocalDeveloperMode = staticCompositionLocalOf { false }
val LocalThemePreference = compositionLocalOf<ThemePreference>{
    error("ThemePreference not initialized!")
}
val LocalNavController = compositionLocalOf<NavHostController> {
    error("LocalNavController not initialized!")
}
val LocalSlidingDrawerState = compositionLocalOf<SlidingDrawerState> {
    error("LocalSlidingDrawerState not initialized!")
}