package com.diffy.broke.presentation.accounthead

sealed interface AccountHeadOneTimeEvent {
    data class Success(val message: String) : AccountHeadOneTimeEvent
    data class Error(val message: String) : AccountHeadOneTimeEvent
}