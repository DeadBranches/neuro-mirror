package com.omnivoiceai.neuromirror.data.database.thread

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

enum class MessageRole {
    USER, ASSISTANT, SYSTEM
}

enum class MessageType {
    TEXT, IMAGE, FILE, AUDIO
}

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Thread::class,
            parentColumns = ["id"],
            childColumns = ["thread_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["thread_id"])
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "thread_id") val threadId: Int,
    @ColumnInfo val role: MessageRole,
    @ColumnInfo val content: String,
    @ColumnInfo val timestamp: Date,
    @ColumnInfo(name = "message_type") val messageType: MessageType = MessageType.TEXT,
    @ColumnInfo(name = "firebase_id") val firebaseId: String? = null,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean = false
) 