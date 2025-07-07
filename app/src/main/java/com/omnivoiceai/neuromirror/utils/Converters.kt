package com.omnivoiceai.neuromirror.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.data.database.question.QuestionType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromList(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun toList(value: String): List<String> =
        Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromEmotionDetected(value: EmotionDetected?): String? {
        return value?.name
    }

    @TypeConverter
    fun toEmotionDetected(value: String?): EmotionDetected? {
        return value?.let { EmotionDetected.valueOf(it) }
    }

    @TypeConverter
    fun fromQuestionType(value: QuestionType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toQuestionType(value: String?): QuestionType? {
        return value?.let { QuestionType.valueOf(it) }
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = "|||")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split("|||")
    }
} 