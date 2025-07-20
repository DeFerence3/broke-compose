package com.diffy.broke.domain.model

import android.os.Parcelable
import com.diffy.broke.data.entity.Classification
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountGroup(
    val id: Int,
    val accountGroupName: String,
    val accountHeads: List<AccountHead>,
    val classification: Classification,
    val parentGroup: AccountGroup? = null,
    val description: String?
) : Parcelable{
    companion object {
        fun fromEntity(entity: com.diffy.broke.data.entity.AccountGroup): AccountGroup {
            return AccountGroup(
                id = entity.id,
                accountGroupName = entity.name,
                accountHeads = emptyList(),
                classification = entity.classification,
                parentGroup = null,
                description = entity.description
            )
        }
    }
}
