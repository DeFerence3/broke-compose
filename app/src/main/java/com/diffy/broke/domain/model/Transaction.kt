package com.diffy.broke.domain.model

import com.diffy.broke.data.relations.TransactionWithAccountHeads
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import com.diffy.broke.data.entity.Transaction as TransactionEntity


@ExperimentalTime
data class Transaction(
    val id: Int,
    val notes: String,
    val amount: String?,
    val day: Long,
    val fromAccountHead: AccountHead,
    val toAccountHead: AccountHead
) {
    fun toEntity(): TransactionEntity {
        return TransactionEntity(
            id = id,
            notes = notes,
            amount = amount?.toFloatOrNull() ?: 0F,
            date = Instant.fromEpochMilliseconds(day),
            debitAccountHeadId = fromAccountHead.id,
            creditAccountHeadId = toAccountHead.id
        )
    }

    companion object {
        fun fromEntity(entity: TransactionWithAccountHeads): com.diffy.broke.domain.model.Transaction {
            return Transaction(
                id = entity.transaction.id,
                notes = entity.transaction.notes,
                amount = entity.transaction.amount.toString(),
                day = entity.transaction.date.toEpochMilliseconds(),
                fromAccountHead = AccountHead.fromEntity(entity.debitAccountHead),
                toAccountHead = AccountHead.fromEntity(entity.creditAccountHead)
            )
        }
    }
}
