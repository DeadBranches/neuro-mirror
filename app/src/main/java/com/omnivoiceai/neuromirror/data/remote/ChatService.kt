package com.omnivoiceai.neuromirror.data.remote
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AgentType {
    @SerialName("neuromirror_question")
    QuestionIntrospection,
    @SerialName("neuromirror_chat_conversation")
    ChatIntrospection
}

@Serializable
data class SendMessageRequest(
    val action: String = "sendMessage",
    val threadId: String = "",
    val messageType: String = "text",
    val messageContent: String,
    val imageDescription: String? = null,
    val agentType: AgentType
)

// New data classes for actual server response format
@Serializable
data class ServerResponse(
    val threadId: String = "",
    val assistantMessage: List<ServerAssistantMessage> = emptyList()
)

@Serializable
data class ServerAssistantMessage(
    val text: ServerTextWrapper
)

@Serializable
data class ServerTextWrapper(
    val value: ServerMessageContent
)

@Serializable
data class QuestionData(
    val title: String,
    val type: String,
    val options: List<String> = emptyList(),
    val correctIndex: Int? = null
)

@Serializable
data class ServerMessageContent(
    val body: String,
    val sources: List<String> = emptyList(),
    val questions: List<QuestionData> = emptyList()
)

@Serializable
data class SendMessageResponse(
    val threadId: String = "",
    val assistantMessage: List<AssistantMessage> = emptyList()
)

@Serializable
data class AssistantMessage(
    val text: TextWrapper
)

@Serializable
data class TextWrapper(
    val value: String
)

interface ChatService {
    @POST("chat")
    @Headers("Content-Type: application/json")
    suspend fun sendMessage(
        @Body body: SendMessageRequest
    ): ServerResponse
}
