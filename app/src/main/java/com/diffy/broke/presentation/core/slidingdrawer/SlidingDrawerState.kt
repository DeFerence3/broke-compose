package com.diffy.broke.presentation.core.slidingdrawer

enum class SlidingDrawerState {
    Opened,
    Closed;

    fun isOpened(): Boolean = this == Opened

    fun opposite(): SlidingDrawerState = if (this == Opened) Closed else Opened
}

