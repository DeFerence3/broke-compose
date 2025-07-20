package com.diffy.broke.presentation.config

sealed interface ConfigEvent{
    data object OnSubmit: ConfigEvent
}