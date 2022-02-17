package com.developer.finance.featureExpenseManagement

object Constants {

    val transactionTypes = listOf(
        "overall",
        "income",
        "expense"
    )

    val transactionCategory = listOf(
        "all",
        "housing",
        "transportation",
        "food",
        "utilities",
        "insurance",
        "healthcare",
        "saving & debts",
        "personal spending",
        "entertainment",
        "miscellaneous"
    )

    val transactionFrequency = listOf(
        "none",
        "everyday",
        "every week",
        "every month"
    )

    val transactionFrequencyToMillis = mapOf(
        "none" to 0,
        "everyday" to 86400000,
        "every week" to 604800000,
        "every month" to 2628000000
    )
}