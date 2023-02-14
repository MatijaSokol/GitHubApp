package com.matijasokol.repo_domain

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    fun fromStringToDate(date: String): Date {
        return dateFormatter.parse(date)
    }
}