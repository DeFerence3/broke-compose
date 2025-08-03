package com.diffy.broke.presentation.core.enums

sealed interface ViewType{
    data object Today: ViewType
    data object ThisWeek: ViewType
    data object ThisMonth: ViewType
    data class Custom(val start: Long,val end: Long): ViewType
}