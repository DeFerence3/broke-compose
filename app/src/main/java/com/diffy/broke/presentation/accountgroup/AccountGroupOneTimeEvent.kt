package com.diffy.broke.presentation.accountgroup

sealed interface AccountGroupOneTimeEvent {
    data class Success(val message: String) : AccountGroupOneTimeEvent
    data class Error(val message: String) : AccountGroupOneTimeEvent
}