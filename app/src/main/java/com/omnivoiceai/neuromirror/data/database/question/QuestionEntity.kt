package com.omnivoiceai.neuromirror.data.database.question

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

enum class QuestionType { Oneshot, Multiple, ShortText, LongText }

@Entity()
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "note_id") val noteId: Int,
    @ColumnInfo() val title: String?,
    @ColumnInfo() val type: QuestionType,
)

@Entity()
data class OneShotQuestion(
    @PrimaryKey val questionId: Int,
    @ColumnInfo() val answers: List<String>
)

@Entity()
data class MultipleChoiceQuestion(
    @PrimaryKey val questionId: Int,
    @ColumnInfo() val options: List<String>,
    @ColumnInfo(name = "correct_index") val correctIndex: Int
)

data class QuestionWithDetails(
    @Embedded val question: Question,

    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val oneShotData: OneShotQuestion?,

    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val multipleChoiceData: MultipleChoiceQuestion?
)
