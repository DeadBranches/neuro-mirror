package com.omnivoiceai.neuromirror.data.database.note

import androidx.annotation.StringRes
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.data.database.question.Question
import java.util.Date

enum class EmotionDetected {
    Sadness, Anger, Love, Surprise, Fear,
    Happiness, Neutral, Disgust, Shame, Guilt,
    Confusion, Desire, Sarcasm;

    @StringRes
    fun getLabelRes(): Int = when (this) {
        Sadness -> R.string.emotion_sadness
        Anger -> R.string.emotion_anger
        Love -> R.string.emotion_love
        Surprise -> R.string.emotion_surprise
        Fear -> R.string.emotion_fear
        Happiness -> R.string.emotion_happiness
        Neutral -> R.string.emotion_neutral
        Disgust -> R.string.emotion_disgust
        Shame -> R.string.emotion_shame
        Guilt -> R.string.emotion_guilt
        Confusion -> R.string.emotion_confusion
        Desire -> R.string.emotion_desire
        Sarcasm -> R.string.emotion_sarcasm
    }
}

fun EmotionDetected?.toEmoji(): String {
    return when (this) {
        EmotionDetected.Sadness -> "😢"
        EmotionDetected.Anger -> "😠"
        EmotionDetected.Love -> "❤️"
        EmotionDetected.Surprise -> "😲"
        EmotionDetected.Fear -> "😱"
        EmotionDetected.Happiness -> "😄"
        EmotionDetected.Neutral -> "😐"
        EmotionDetected.Disgust -> "🤢"
        EmotionDetected.Shame -> "🙈"
        EmotionDetected.Guilt -> "😔"
        EmotionDetected.Confusion -> "😕"
        EmotionDetected.Desire -> "🔥"
        EmotionDetected.Sarcasm -> "😏"
        null -> "❓"
    }
}


@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val title: String? = null,
    @ColumnInfo val content: String,
    @ColumnInfo(name="is_evaluated") val isEvaluated: Boolean = false,
    @ColumnInfo(name = "firebase_id") val firebaseId: String? = null,
    @ColumnInfo(name = "emotion_detected") val emotionDetected: EmotionDetected? = null,
    @ColumnInfo(name = "created_at") val createdAt: Date,
)

data class NoteWithQuestions(
    @Embedded val note: Note,

    @Relation(
        parentColumn = "id",
        entityColumn = "note_id"
    )
    val questions: List<Question>
)
