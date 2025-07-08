package com.omnivoiceai.neuromirror.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.note.NoteDao
import com.omnivoiceai.neuromirror.data.database.question.MultipleChoiceQuestion
import com.omnivoiceai.neuromirror.data.database.question.OneShotQuestion
import com.omnivoiceai.neuromirror.data.database.question.Question
import com.omnivoiceai.neuromirror.data.database.question.QuestionAnswer
import com.omnivoiceai.neuromirror.data.database.question.QuestionDAO
import com.omnivoiceai.neuromirror.data.database.thread.Message
import com.omnivoiceai.neuromirror.data.database.thread.Thread
import com.omnivoiceai.neuromirror.data.database.thread.ThreadDAO
import com.omnivoiceai.neuromirror.utils.Converters

@Database(
    entities = [
        Note::class,
        Question::class,
        OneShotQuestion::class,
        MultipleChoiceQuestion::class,
        QuestionAnswer::class,
        Thread::class,
        Message::class
    ],
    version = 3
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun questionDao(): QuestionDAO
    abstract fun threadDao(): ThreadDAO
}
