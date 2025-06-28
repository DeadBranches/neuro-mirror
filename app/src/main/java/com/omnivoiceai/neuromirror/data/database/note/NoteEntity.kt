package com.omnivoiceai.neuromirror.data.database.note

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.omnivoiceai.neuromirror.data.database.question.Question
import java.util.Date

enum class EmotionDetected { Sadness, Anger, Love, Surprise, Fear,
                             Happiness, Neutral, Disgust, Shame, Guilt,
                             Confusion, Desire, Sarcasm }

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
