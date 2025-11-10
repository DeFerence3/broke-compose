package com.diffy.broke.domain.model

import com.diffy.broke.data.relations.TransactionWithCategory
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import com.diffy.broke.data.entity.Transaction as TransactionEntity


@ExperimentalTime
data class Transaction(
    val id: Int,
    val notes: String,
    val amount: String,
    val day: Long,
    val category: Category,
    val isIncome: Boolean,
    val mode: TransactionFormModes = TransactionFormModes.ADD
) {
    fun toEntity(): TransactionEntity {
        return TransactionEntity(
            id = id,
            notes = notes,
            amount = amount.toFloatOrNull() ?: 0F,
            date = Instant.fromEpochMilliseconds(day),
            categoryId = category.id,
            transactionGroupId = null,
            isIncome = isIncome
        )
    }

    companion object {
        fun fromEntity(entity: TransactionWithCategory): Transaction {
            return Transaction(
                id = entity.transaction.id,
                notes = entity.transaction.notes,
                amount = entity.transaction.amount.toString(),
                day = entity.transaction.date.toEpochMilliseconds(),
                category = Category.fromEntity(entity.category),
                isIncome = entity.transaction.isIncome
            )
        }

        fun new() = Transaction(
            id = 0,
            notes = "",
            amount = "",
            day = System.currentTimeMillis(),
            category = Category.new(),
            mode = TransactionFormModes.ADD,
            isIncome = false
        )
    }
}
