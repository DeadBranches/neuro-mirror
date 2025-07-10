package com.omnivoiceai.neuromirror.data.database.thread

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.omnivoiceai.neuromirror.data.database.note.Note
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["note_id"])
    ]
)
data class Thread(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "note_id") val noteId: Int,
    @ColumnInfo(name = "note_firebase_id") val noteFirebaseId: String? = null,
    @ColumnInfo val title: String,
    @ColumnInfo val date: Date,
    @ColumnInfo(name = "last_updated") val lastUpdated: Date,
    @ColumnInfo(name = "firebase_id") val firebaseId: String? = null,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean = false
)

data class ThreadWithMessages(
    @Embedded val thread: Thread,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "thread_id"
    )
    val messages: List<Message>
)

data class ThreadWithNote(
    @Embedded val thread: Thread,
    
    @Relation(
        parentColumn = "note_id",
        entityColumn = "id"
    )
    val note: Note?
) 