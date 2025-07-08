package com.omnivoiceai.neuromirror.data.database.thread

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ThreadDAO {
    
    // Thread operations
    @Query("SELECT * FROM Thread ORDER BY last_updated DESC")
    fun getAllThreads(): Flow<List<Thread>>
    
    @Query("SELECT * FROM Thread WHERE note_id = :noteId")
    fun getThreadsByNoteId(noteId: Int): Flow<List<Thread>>
    
    @Query("SELECT * FROM Thread WHERE id = :id")
    suspend fun getThreadById(id: Int): Thread?
    
    @Transaction
    @Query("SELECT * FROM Thread WHERE id = :id")
    suspend fun getThreadWithMessages(id: Int): ThreadWithMessages?
    
    @Transaction
    @Query("SELECT * FROM Thread WHERE note_id = :noteId ORDER BY last_updated DESC")
    fun getThreadsWithMessagesByNoteId(noteId: Int): Flow<List<ThreadWithMessages>>
    
    @Transaction
    @Query("SELECT * FROM Thread ORDER BY last_updated DESC")
    fun getAllThreadsWithMessages(): Flow<List<ThreadWithMessages>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThread(thread: Thread): Long
    
    @Update
    suspend fun updateThread(thread: Thread)
    
    @Delete
    suspend fun deleteThread(thread: Thread)
    
    @Query("DELETE FROM Thread WHERE note_id = :noteId")
    suspend fun deleteThreadsByNoteId(noteId: Int)
    
    // Message operations
    @Query("SELECT * FROM messages WHERE thread_id = :threadId ORDER BY timestamp ASC")
    fun getMessagesByThreadId(threadId: Int): Flow<List<Message>>
    
    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getMessageById(id: Int): Message?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<Message>)
    
    @Update
    suspend fun updateMessage(message: Message)
    
    @Delete
    suspend fun deleteMessage(message: Message)
    
    @Query("DELETE FROM messages WHERE thread_id = :threadId")
    suspend fun deleteMessagesByThreadId(threadId: Int)
    
    // Sync operations for Firebase
    @Query("SELECT * FROM Thread WHERE is_synced = 0")
    suspend fun getUnsyncedThreads(): List<Thread>
    
    @Query("SELECT * FROM messages WHERE is_synced = 0")
    suspend fun getUnsyncedMessages(): List<Message>
    
    @Query("UPDATE Thread SET is_synced = 1, firebase_id = :firebaseId WHERE id = :id")
    suspend fun markThreadAsSynced(id: Int, firebaseId: String)
    
    @Query("UPDATE messages SET is_synced = 1, firebase_id = :firebaseId WHERE id = :id")
    suspend fun markMessageAsSynced(id: Int, firebaseId: String)
    
    // Utility queries
    @Query("SELECT COUNT(*) FROM Thread WHERE note_id = :noteId")
    suspend fun getThreadCountByNoteId(noteId: Int): Int
    
    @Query("SELECT COUNT(*) FROM messages WHERE thread_id = :threadId")
    suspend fun getMessageCountByThreadId(threadId: Int): Int
} 