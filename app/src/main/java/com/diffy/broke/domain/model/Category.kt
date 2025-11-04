package com.diffy.broke.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import com.diffy.broke.data.entity.Category as entity

@Parcelize
@Serializable
data class Category(
    val id: Int,
    val categoryName: String
): Parcelable {
    companion object {
        fun fromEntity(entity: entity): Category {
            return Category(
                id = entity.id,
                categoryName = entity.categoryName,
            )
        }

        fun new() = Category(
            id = 0,
            categoryName = "",
        )
    }
}
