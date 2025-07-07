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

@Entity(tableName = "question_answers")
data class QuestionAnswer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "question_id") val questionId: Int,
    @ColumnInfo(name = "answer_text") val answerText: String? = null,
    @ColumnInfo(name = "selected_option_index") val selectedOptionIndex: Int? = null,
    @ColumnInfo(name = "selected_option_text") val selectedOptionText: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
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

data class QuestionWithAnswer(
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
    val multipleChoiceData: MultipleChoiceQuestion?,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "question_id"
    )
    val answer: QuestionAnswer?
)
