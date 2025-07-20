package com.diffy.broke.data

import com.diffy.broke.data.entity.AccountGroup
import com.diffy.broke.data.entity.AccountHead
import com.diffy.broke.data.entity.BalanceType
import com.diffy.broke.data.entity.Classification

val defaultAccountGroups = listOf(
    AccountGroup(
        id = 1,
        name = "Capital Account",
        classification = Classification.Capital,
        parentGroupId = null,
        description = "Net Worth"
    ),
    AccountGroup(
        id = 2,
        name = "Current Assets",
        classification = Classification.Asset,
        parentGroupId = null,
        description = "Things you own or have in hand"
    ),
    AccountGroup(
        id = 3,
        name = "Bank Accounts",
        classification = Classification.Asset,
        parentGroupId = 2,
        description = "Your bank savings accounts"
    ),
    AccountGroup(
        id = 4,
        name = "Cash-in-Hand",
        classification = Classification.Asset,
        parentGroupId = 2,
        description = "Physical cash"
    ),
    AccountGroup(
        id = 5,
        name = "Receivables",
        classification = Classification.Asset,
        parentGroupId = 2,
        description = "Money owed to you by friends, relatives"
    ),
    AccountGroup(
        id = 6,
        name = "Investments",
        classification = Classification.Asset,
        parentGroupId = null,
        description = "Stocks, FDs, Mutual Funds"
    ),
    AccountGroup(
        id = 7,
        name = "Current Liabilities",
        classification = Classification.Liability,
        parentGroupId = null,
        description = "Things you owe"
    ),
    AccountGroup(
        id = 8,
        name = "Payables",
        classification = Classification.Liability,
        parentGroupId = 7,
        description = "Money you owe to friends, credit cards"
    ),
    AccountGroup(
        id = 9,
        name = "Income",
        classification = Classification.Income,
        parentGroupId = null,
        description = "Your income sources"
    ),
    AccountGroup(
        id = 10,
        name = "Salary",
        classification = Classification.Income,
        parentGroupId = 9,
        description = "Salary income"
    ),
    AccountGroup(
        id = 11,
        name = "Other Income",
        classification = Classification.Income,
        parentGroupId = 9,
        description = "Rent, Interest, Gifts received"
    ),
    AccountGroup(
        id = 12,
        name = "Expenses",
        classification = Classification.Expense,
        parentGroupId = null,
        description = "All expenses"
    ),
    AccountGroup(
        id = 13,
        name = "Household Expenses",
        classification = Classification.Expense,
        parentGroupId = 12,
        description = "Rent, Groceries, Utilities"
    ),
    AccountGroup(
        id = 14,
        name = "Personal Expenses",
        classification = Classification.Expense,
        parentGroupId = 12,
        description = "Clothes, Gadgets, Subscriptions"
    ),
    AccountGroup(
        id = 15,
        name = "Food & Dining",
        classification = Classification.Expense,
        parentGroupId = 12,
        description = "Restaurants, Takeaway"
    ),
    AccountGroup(
        id = 16,
        name = "Transport",
        classification = Classification.Expense,
        parentGroupId = 12,
        description = "Fuel, Cab, Repairs"
    ),
    AccountGroup(
        id = 17,
        name = "Health",
        classification = Classification.Expense,
        parentGroupId = 12,
        description = "Medicines, Doctor Fees"
    ),
    AccountGroup(
        id = 18,
        name = "Entertainment",
        classification = Classification.Expense,
        parentGroupId = 12,
        description = "Movies, Parties, Streaming"
    ),
    AccountGroup(
        id = 19,
        name = "Miscellaneous",
        classification = Classification.Expense,
        parentGroupId = 12,
        description = "Unexpected stuff"
    )
)

val defaultAccountHeads = listOf(
    // ➖ Capital
    AccountHead(
        id = 1,
        accountHeadName = "Owner's Capital",
        accountGroupId = 1, // Capital Account
        openingBalance = 0.0,
        balanceType = BalanceType.Credit
    ),

    // ➕ Bank & Cash
    AccountHead(
        id = 2,
        accountHeadName = "HDFC Bank",
        accountGroupId = 3, // Bank Accounts
        openingBalance = 10000.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 3,
        accountHeadName = "Cash in Wallet",
        accountGroupId = 4, // Cash-in-Hand
        openingBalance = 1000.0,
        balanceType = BalanceType.Debit
    ),

    // ➕ Receivable Example
    AccountHead(
        id = 4,
        accountHeadName = "Receivable from Friend A",
        accountGroupId = 5, // Receivables
        openingBalance = 2000.0,
        balanceType = BalanceType.Debit
    ),

    // ➖ Payable Example
    AccountHead(
        id = 5,
        accountHeadName = "Credit Card Outstanding",
        accountGroupId = 8, // Payables
        openingBalance = 5000.0,
        balanceType = BalanceType.Credit
    ),

    // ➕ Investments (if you want)
    AccountHead(
        id = 6,
        accountHeadName = "Mutual Fund Investment",
        accountGroupId = 6, // Investments
        openingBalance = 25000.0,
        balanceType = BalanceType.Debit
    ),

    // ➕ Income Heads (these usually don’t have opening balances)
    AccountHead(
        id = 7,
        accountHeadName = "Salary Income",
        accountGroupId = 10, // Salary
        openingBalance = 0.0,
        balanceType = BalanceType.Credit
    ),
    AccountHead(
        id = 8,
        accountHeadName = "Gift Income",
        accountGroupId = 11, // Other Income
        openingBalance = 0.0,
        balanceType = BalanceType.Credit
    ),

    // ➖ Expense Heads (also no opening balance)
    AccountHead(
        id = 9,
        accountHeadName = "House Rent",
        accountGroupId = 13, // Household Expenses
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 10,
        accountHeadName = "Groceries",
        accountGroupId = 13, // Household Expenses
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 11,
        accountHeadName = "Internet Bill",
        accountGroupId = 13, // Household Expenses
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 12,
        accountHeadName = "Clothing & Accessories",
        accountGroupId = 14, // Personal Expenses
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 13,
        accountHeadName = "Dining Out",
        accountGroupId = 15, // Food & Dining
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 14,
        accountHeadName = "Fuel",
        accountGroupId = 16, // Transport
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 15,
        accountHeadName = "Doctor Visits",
        accountGroupId = 17, // Health
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 16,
        accountHeadName = "Netflix Subscription",
        accountGroupId = 18, // Entertainment
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    ),
    AccountHead(
        id = 17,
        accountHeadName = "Miscellaneous Expenses",
        accountGroupId = 19, // Miscellaneous
        openingBalance = 0.0,
        balanceType = BalanceType.Debit
    )
)