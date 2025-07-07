package com.omnivoiceai.neuromirror.data.repositories

import android.content.Context
import com.omnivoiceai.neuromirror.data.remote.AgentType
import com.omnivoiceai.neuromirror.data.remote.ChatIntrospectionResponse
import com.omnivoiceai.neuromirror.data.remote.ChatService
import com.omnivoiceai.neuromirror.data.remote.IntrospectionResponse
import com.omnivoiceai.neuromirror.data.remote.QuestionIntrospectionResponse
import com.omnivoiceai.neuromirror.data.remote.ResponseParser
import com.omnivoiceai.neuromirror.data.remote.ResponseParsers
import com.omnivoiceai.neuromirror.data.remote.SendMessageRequest
import com.omnivoiceai.neuromirror.utils.Logger
import com.omnivoiceai.neuromirror.utils.encryptMessage
import kotlinx.serialization.json.Json
import org.json.JSONObject

class IntrospectionRepository(private val api: ChatService) {

    suspend fun <T : IntrospectionResponse> sendMessage(
        context: Context,
        userMessage: String,
        threadId: String,
        agentType: AgentType,
        parser: ResponseParser<T>,
        isJson: Boolean = false
    ): T {
        val publicKeyPath = "public.key"
        val plainText = if (isJson) JSONObject(userMessage).toString() else userMessage
        val encryptedMessage = encryptMessage(context, plainText, publicKeyPath)

        val request = SendMessageRequest(
            messageContent = encryptedMessage,
            threadId = threadId,
            agentType = agentType
        )

        Logger.info("SendMessageRequest:\n$request")
        Logger.info("Serialized:\n${Json.encodeToString(SendMessageRequest.serializer(), request)}")

        return try {
            val response = api.sendMessage(request)
            
            val messageContent = response.assistantMessage.firstOrNull()?.text?.value
                ?: throw IllegalStateException("Empty response from server")
            
            @Suppress("UNCHECKED_CAST")
            when (agentType) {
                AgentType.QuestionIntrospection -> {
                    val questionResponse = QuestionIntrospectionResponse(
                        body = messageContent.body,
                        questions = messageContent.questions
                    )
                    questionResponse as T
                }
                AgentType.ChatIntrospection -> {
                    val chatResponse = ChatIntrospectionResponse(
                        body = messageContent.body,
                        sources = messageContent.sources
                    )
                    chatResponse as T
                }
            }
        } catch (e: Exception) {
            Logger.error("Error sending message", e)
            throw e
        }
    }

    suspend fun sendQuestionRequest(
        context: Context,
        userMessage: String,
        threadId: String,
        isJson: Boolean = false
    ): QuestionIntrospectionResponse {
        return sendMessage(
            context = context,
            userMessage = userMessage,
            threadId = threadId,
            agentType = AgentType.QuestionIntrospection,
            parser = ResponseParsers.questionParser,
            isJson = isJson
        )
    }

    suspend fun sendChatRequest(
        context: Context,
        userMessage: String,
        threadId: String
    ): ChatIntrospectionResponse {
        return sendMessage(
            context = context,
            userMessage = userMessage,
            threadId = threadId,
            agentType = AgentType.ChatIntrospection,
            parser = ResponseParsers.chatParser
        )
    }


} 