package com.diffy.broke.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Classification: Parcelable {
    Asset,
    Liability,
    Income,
    Expense,
    Capital
}