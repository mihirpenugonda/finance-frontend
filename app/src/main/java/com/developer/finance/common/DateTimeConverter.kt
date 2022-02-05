package com.developer.finance.common

import java.text.SimpleDateFormat
import java.util.*

class DateTimeConverter() {

    fun format(time: Long): String {
        val format = "dd/MM/yy"
        val sdf = SimpleDateFormat(format, Locale.US)
        return sdf.format(time)
    }

    fun formatToMillis(date: String): Long {
        val format = "dd/MM/yy"
        val sdf = SimpleDateFormat(format, Locale.US)
        val dateObject = sdf.parse(date)

        return dateObject!!.time
    }

}