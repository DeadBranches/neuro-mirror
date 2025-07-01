package com.omnivoiceai.neuromirror.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.NoteDao
import com.omnivoiceai.neuromirror.data.database.question.MultipleChoiceQuestion
import com.omnivoiceai.neuromirror.data.database.question.OneShotQuestion
import com.omnivoiceai.neuromirror.data.database.question.Question
import com.omnivoiceai.neuromirror.data.database.question.QuestionDao
import com.omnivoiceai.neuromirror.utils.Converters

@Database(
    entities = [
        Note::class,
        Question::class,
        OneShotQuestion::class,
        MultipleChoiceQuestion::class
    ],
    version = 1
)
@TypeConverters(Converters
::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun questionDao(): QuestionDao
}
