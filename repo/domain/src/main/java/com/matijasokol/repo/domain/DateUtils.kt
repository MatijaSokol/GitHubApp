package com.matijasokol.repo.domain

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {

    private val apiDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    private val localDateFormatter = SimpleDateFormat("HH:mm dd.MM.yyyy")

    fun fromStringToDate(date: String): Date = apiDateFormatter.parse(date)

    fun fromDateToString(date: Date): String = apiDateFormatter.format(date)

    fun dateToLocalDateString(date: Date): String = localDateFormatter.format(date)
}
