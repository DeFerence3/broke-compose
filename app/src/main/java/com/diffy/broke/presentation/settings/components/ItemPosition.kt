package com.diffy.settings.ui.settings.components

sealed class ItemPosition {
    data object Top : ItemPosition()
    data object Middle : ItemPosition()
    data object Bottom : ItemPosition()
    data object Alone : ItemPosition()
}