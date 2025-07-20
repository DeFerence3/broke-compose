package com.diffy.broke.presentation.core.search

import android.os.Parcelable

data class SearchResult(
    var item: Parcelable,
    var name: String,
    var subTitle: String? = null
)