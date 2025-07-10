package com.omnivoiceai.neuromirror.worker

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.omnivoiceai.neuromirror.data.database.AppDatabase
import com.omnivoiceai.neuromirror.data.database.badge.Badge
import com.omnivoiceai.neuromirror.data.database.note.Note
import com.omnivoiceai.neuromirror.data.database.question.MultipleChoiceQuestion
import com.omnivoiceai.neuromirror.data.database.question.OneShotQuestion
import com.omnivoiceai.neuromirror.data.database.question.Question
import com.omnivoiceai.neuromirror.data.database.question.QuestionAnswer
import com.omnivoiceai.neuromirror.data.database.thread.Message
import com.omnivoiceai.neuromirror.data.database.thread.Thread
import com.omnivoiceai.neuromirror.domain.model.Operation
import com.omnivoiceai.neuromirror.utils.Logger
import com.omnivoiceai.neuromirror.utils.UiEventBus
import org.json.JSONObject
import java.io.OutputStreamWriter

class ExportImportWorker(
    private val db: AppDatabase,
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override suspend fun doWork(): Result {
        val action = inputData.getString("action") ?: return Result.failure()
        return try {
            when (Operation.valueOf(action.uppercase())) {
                Operation.EXPORT -> exportData()
                Operation.IMPORT -> importData()
            }
        } catch (e: Exception) {
            Logger.error("ExportImportWorker failed", e)
            Result.failure()
        }
    }

    private suspend fun exportData(): Result {
        try {
            val notes = db.noteDao().getAllRaw()
            val badges = db.badgeDao().getAllRaw()
            val questions = db.questionDao().getAll()
            val oneShots = db.questionDao().getAllOneShots()
            val multipleChoices = db.questionDao().getAllMultipleChoice()
            val answers = db.questionDao().getQuestionsWithAnswers()
            val threads = db.threadDao().getAllThreadsRaw()
            val messages = db.threadDao().getAllMessagesRaw()

            val backupData = mapOf(
                "notes" to notes,
                "badges" to badges,
                "questions" to questions,
                "oneShots" to oneShots,
                "multipleChoices" to multipleChoices,
                "answers" to answers,
                "threads" to threads,
                "messages" to messages
            )

            val json = gson.toJson(backupData)
            val export = inputData.getString("export_uri") ?: return Result.failure()
            Logger.d(export)
            val outputStream = applicationContext.contentResolver.openOutputStream(export.toUri())
            OutputStreamWriter(outputStream).use { it.write(json) }
            outputStream?.flush()
            outputStream?.close()
//            File(export).writeText(json)

            Logger.d("Export completed with ${backupData.size} categories")
            UiEventBus.showNotification("Exported with success")
            return Result.success()
        } catch (e: Exception) {
            Logger.error("Export failed", e)
            return Result.failure()
        }
    }

    private fun getJsonFromPickedFile(context: Context, uri: Uri): JSONObject? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val json = inputStream?.bufferedReader().use { it?.readText() ?: "" }
            if (json.isBlank()) null else JSONObject(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun importData(): Result {

        val uriString = inputData.getString("import_uri") ?: return Result.failure()
        val uri = uriString.toUri()

        val jsonObject = getJsonFromPickedFile(applicationContext, uri)
            ?: return Result.failure()

        val noteList: List<Note> = gson.fromJson(
            jsonObject.getJSONArray("notes").toString(),
            object : TypeToken<List<Note>>() {}.type
        )

        val badgeList: List<Badge> = gson.fromJson(
            jsonObject.getJSONArray("badges").toString(),
            object : TypeToken<List<Badge>>() {}.type
        )

        val questionList: List<Question> = gson.fromJson(
            jsonObject.getJSONArray("questions").toString(),
            object : TypeToken<List<Question>>() {}.type
        )

        val oneShots: List<OneShotQuestion> = gson.fromJson(
            jsonObject.getJSONArray("oneShots").toString(),
            object : TypeToken<List<OneShotQuestion>>() {}.type
        )

        val multipleChoices: List<MultipleChoiceQuestion> = gson.fromJson(
            jsonObject.getJSONArray("multipleChoices").toString(),
            object : TypeToken<List<MultipleChoiceQuestion>>() {}.type
        )

        val answers: List<QuestionAnswer> = gson.fromJson(
            jsonObject.getJSONArray("answers").toString(),
            object : TypeToken<List<QuestionAnswer>>() {}.type
        )

        val threads: List<Thread> = gson.fromJson(
            jsonObject.getJSONArray("threads").toString(),
            object : TypeToken<List<Thread>>() {}.type
        )

        val messages: List<Message> = gson.fromJson(
            jsonObject.getJSONArray("messages").toString(),
            object : TypeToken<List<Message>>() {}.type
        )


        db.noteDao().insertAll(noteList)
        db.badgeDao().insertAll(badgeList)
        db.questionDao().insertAllQuestions(questionList)
        db.questionDao().insertAllOneShots(oneShots)
        db.questionDao().insertAllMultipleChoices(multipleChoices)
        db.questionDao().insertAllAnswers(answers)
        db.threadDao().insertAll(threads)
        db.threadDao().insertMessages(messages)

        Logger.d("Import completed with ${noteList.size} notes, ${threads.size} threads")
        UiEventBus.showNotification("Imported with success")
        return Result.success()
    }
}
