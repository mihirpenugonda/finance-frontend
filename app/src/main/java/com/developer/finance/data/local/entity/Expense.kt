package com.developer.finance.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "amount") var amount: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "created_at") var created_at: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "e_id")
    var id: Int = 0,
)  {

}