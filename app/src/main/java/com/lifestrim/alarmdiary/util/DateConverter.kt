package com.lifestrim.alarmdiary.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {

    fun getDayMonthHoursMinute(date: Date): String? {
        val testDate = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date)
        return testDate.toString()
    }
}