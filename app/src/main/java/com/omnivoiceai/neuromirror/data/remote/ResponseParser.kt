package com.omnivoiceai.neuromirror.data.remote

import com.omnivoiceai.neuromirror.utils.Logger
import kotlinx.serialization.json.Json
import org.json.JSONObject

interface ResponseParser<T : IntrospectionResponse> {
    fun parse(responseText: String): T
}

class QuestionResponseParser : ResponseParser<QuestionIntrospectionResponse> {
    override fun parse(responseText: String): QuestionIntrospectionResponse {
        return try {
            Json.decodeFromString<QuestionIntrospectionResponse>(responseText)
        } catch (e: Exception) {
            Logger.error("Error parsing question response", e)
            // Fallback parsing
            parseQuestionResponseFallback(responseText)
        }
    }
    
    private fun parseQuestionResponseFallback(responseText: String): QuestionIntrospectionResponse {
        return try {
            val jsonObject = JSONObject(responseText)
            val body = jsonObject.optString("body", "")
            val questionsArray = jsonObject.optJSONArray("questions")
            
            val questions = mutableListOf<QuestionData>()
            questionsArray?.let { array ->
                for (i in 0 until array.length()) {
                    val questionObj = array.getJSONObject(i)
                    val title = questionObj.optString("title", "")
                    val type = questionObj.optString("type", "")
                    val optionsArray = questionObj.optJSONArray("options")
                    val correctIndex = questionObj.optInt("correctIndex", -1).takeIf { it >= 0 }
                    
                    val options = mutableListOf<String>()
                    optionsArray?.let { opts ->
                        for (j in 0 until opts.length()) {
                            options.add(opts.getString(j))
                        }
                    }
                    
                    questions.add(QuestionData(title, type, options, correctIndex))
                }
            }
            
            QuestionIntrospectionResponse(body, questions)
        } catch (e: Exception) {
            Logger.error("Error in fallback parsing for question response", e)
            QuestionIntrospectionResponse("Error parsing response", emptyList())
        }
    }
}

class ChatResponseParser : ResponseParser<ChatIntrospectionResponse> {
    override fun parse(responseText: String): ChatIntrospectionResponse {
        return try {
            Json.decodeFromString<ChatIntrospectionResponse>(responseText)
        } catch (e: Exception) {
            Logger.error("Error parsing chat response", e)
            // Fallback parsing
            parseChatResponseFallback(responseText)
        }
    }
    
    private fun parseChatResponseFallback(responseText: String): ChatIntrospectionResponse {
        return try {
            val jsonObject = JSONObject(responseText)
            val body = jsonObject.optString("body", responseText)
            val sourcesArray = jsonObject.optJSONArray("sources")
            
            val sources = mutableListOf<String>()
            sourcesArray?.let { array ->
                for (i in 0 until array.length()) {
                    sources.add(array.getString(i))
                }
            }
            
            ChatIntrospectionResponse(body, sources)
        } catch (e: Exception) {
            Logger.error("Error in fallback parsing for chat response", e)
            ChatIntrospectionResponse(responseText, emptyList())
        }
    }
}

/**
 * Companion object for easy access to parsers
 */
object ResponseParsers {
    val questionParser = QuestionResponseParser()
    val chatParser = ChatResponseParser()
} 