package com.omnivoiceai.neuromirror.data.repositories

import com.omnivoiceai.neuromirror.data.database.thread.Message
import com.omnivoiceai.neuromirror.data.database.thread.MessageRole
import com.omnivoiceai.neuromirror.data.database.thread.Thread
import com.omnivoiceai.neuromirror.data.database.thread.ThreadDAO
import com.omnivoiceai.neuromirror.data.database.thread.ThreadWithMessages
import kotlinx.coroutines.flow.Flow

class ThreadRepository(private val threadDao: ThreadDAO) {
    val threads: Flow<List<Thread>> = threadDao.getAllThreads()
    
    suspend fun upsert(thread: Thread) = threadDao.insertThread(thread)
    suspend fun delete(thread: Thread) = threadDao.deleteThread(thread)
    suspend fun getThreadWithMessages(threadId: Int): ThreadWithMessages? = threadDao.getThreadWithMessages(threadId)
    
    suspend fun upsert(message: Message) = threadDao.insertMessage(message)
    suspend fun delete(message: Message) = threadDao.deleteMessage(message)
    fun getMessagesByThreadId(threadId: Int): Flow<List<Message>> = threadDao.getMessagesByThreadId(threadId)
    
    fun getThreadsWithMessagesByNoteId(noteId: Int): Flow<List<ThreadWithMessages>> = threadDao.getThreadsWithMessagesByNoteId(noteId)

    fun countAllMessages(role: MessageRole = MessageRole.USER) = threadDao.countAllMessages(role)
} 