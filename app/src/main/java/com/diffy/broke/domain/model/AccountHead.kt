package com.diffy.broke.domain.model

import android.os.Parcelable
import com.diffy.broke.data.entity.BalanceType
import com.diffy.broke.data.relations.AccountHeadWithGroup
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountHead(
    val id: Int,
    val accountHeadName: String,
    val accountGroup: AccountGroup?,
    val openingBalance: Double = 0.0,
    val balanceType: String = "Credit",
): Parcelable {
    fun toEntity(): com.diffy.broke.data.entity.AccountHead{
        return com.diffy.broke.data.entity.AccountHead(
            id = 0,
            accountHeadName = accountHeadName,
            accountGroupId = accountGroup!!.id,
            openingBalance = openingBalance,
            balanceType = BalanceType.valueOf(balanceType),
        )
    }

    companion object {
        fun fromEntity(entity: AccountHeadWithGroup): AccountHead {
            return AccountHead(
                id = entity.accountHead.id,
                accountHeadName = entity.accountHead.accountHeadName,
                accountGroup = AccountGroup.fromEntity(entity.accountGroup)
            )
        }

        fun new() = AccountHead(
            id = 0,
            accountHeadName = "",
            accountGroup = null,
        )
    }
}
