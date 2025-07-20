package com.diffy.broke.data.converter

import androidx.room.TypeConverter
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object InstantConverter {

    @TypeConverter
    fun fromString(value: String): Instant? {
        return value.let {
            try {
                Instant.Companion.parse(it)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    @TypeConverter
    fun instantToString(instant: Instant): String {
        return instant.toString()
    }
}