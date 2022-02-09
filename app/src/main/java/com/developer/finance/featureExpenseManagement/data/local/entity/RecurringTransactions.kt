package com.developer.finance.featureExpenseManagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class RecurringTransactions(
    @ColumnInfo(name = "frequency") var frequency: String,
    @ColumnInfo(name = "t_id")
    @PrimaryKey()
    val id: Long = 0
) : Serializable
