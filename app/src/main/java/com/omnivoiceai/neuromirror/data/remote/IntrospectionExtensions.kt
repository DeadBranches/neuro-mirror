package com.omnivoiceai.neuromirror.data.remote

import android.content.Context
import com.omnivoiceai.neuromirror.data.repositories.IntrospectionRepository

suspend fun IntrospectionRepository.generateQuestions(
    context: Context,
    noteContent: String,
    noteId: Int
): QuestionIntrospectionResponse {
    return sendQuestionRequest(
        context = context,
        userMessage = noteContent,
        threadId = noteId.toString(),
        isJson = false
    )
}

suspend fun IntrospectionRepository.startChatIntrospection(
    context: Context,
    initialMessage: String,
    noteId: Int
): ChatIntrospectionResponse {
    return sendChatRequest(
        context = context,
        userMessage = initialMessage,
        threadId = noteId.toString()
    )
}

suspend fun IntrospectionRepository.continueChat(
    context: Context,
    userMessage: String,
    noteId: Int
): ChatIntrospectionResponse {
    return sendChatRequest(
        context = context,
        userMessage = userMessage,
        threadId = noteId.toString()
    )
}

val IntrospectionResponse.bodyText: String
    get() = when (this) {
        is QuestionIntrospectionResponse -> this.body
        is ChatIntrospectionResponse -> this.body
    } 