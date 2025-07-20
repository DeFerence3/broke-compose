package com.diffy.broke.presentation.core.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(transitionDirection: TransitionDirection): EnterTransition {
    return when(transitionDirection){
        TransitionDirection.TO_RIGHT -> slideIntoContainer(
            animationSpec = tween(Time.TWEEN),
            towards = AnimatedContentTransitionScope.SlideDirection.End
        )
        TransitionDirection.TO_LEFT -> slideIntoContainer(
            animationSpec = tween(Time.TWEEN),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
        TransitionDirection.TO_UP -> slideIntoContainer(
            animationSpec = tween(Time.TWEEN),
            towards = AnimatedContentTransitionScope.SlideDirection.Up
        )
        TransitionDirection.TO_DOWN -> slideIntoContainer(
            animationSpec = tween(Time.TWEEN),
            towards = AnimatedContentTransitionScope.SlideDirection.Down
        )
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(transitionDirection: TransitionDirection): ExitTransition {
    return when(transitionDirection) {
        TransitionDirection.TO_RIGHT -> slideOutOfContainer(
            animationSpec = tween(Time.TWEEN),
            towards = AnimatedContentTransitionScope.SlideDirection.End
        )
        TransitionDirection.TO_LEFT -> slideOutOfContainer(
            animationSpec = tween(Time.TWEEN),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
        TransitionDirection.TO_UP -> slideOutOfContainer(
            animationSpec = tween(Time.TWEEN),
            towards = AnimatedContentTransitionScope.SlideDirection.Up
        )
        TransitionDirection.TO_DOWN -> slideOutOfContainer(
            animationSpec = tween(Time.TWEEN),
            towards = AnimatedContentTransitionScope.SlideDirection.Down
        )
    }
}