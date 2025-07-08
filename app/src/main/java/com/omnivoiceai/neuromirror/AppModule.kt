package com.omnivoiceai.neuromirror

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.auth.FirebaseAuth
import com.omnivoiceai.neuromirror.data.database.AppDatabase
import com.omnivoiceai.neuromirror.data.ml.EmotionModel
import com.omnivoiceai.neuromirror.data.remote.ChatService
import com.omnivoiceai.neuromirror.data.remote.createChatService
import com.omnivoiceai.neuromirror.data.repositories.AuthRepository
import com.omnivoiceai.neuromirror.data.repositories.EmotionRepository
import com.omnivoiceai.neuromirror.data.repositories.IntrospectionNeuroImpl
import com.omnivoiceai.neuromirror.data.repositories.IntrospectionRepository
import com.omnivoiceai.neuromirror.data.repositories.NoteRepository
import com.omnivoiceai.neuromirror.data.repositories.ProfileRepository
import com.omnivoiceai.neuromirror.data.repositories.QuestionRepository
import com.omnivoiceai.neuromirror.data.repositories.ThemeRepository
import com.omnivoiceai.neuromirror.data.repositories.ThreadRepository
import com.omnivoiceai.neuromirror.ui.screens.auth.login.LoginViewModel
import com.omnivoiceai.neuromirror.ui.screens.chat.ChatViewModel
import com.omnivoiceai.neuromirror.ui.screens.note_detail.EmotionViewModel
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel
import com.omnivoiceai.neuromirror.ui.screens.profile.ProfileViewModel
import com.omnivoiceai.neuromirror.ui.screens.questions.QuestionViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("theme")

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `question_answers` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `question_id` INTEGER NOT NULL,
                `answer_text` TEXT,
                `selected_option_index` INTEGER,
                `selected_option_text` TEXT,
                `created_at` INTEGER NOT NULL
            )
        """
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create Thread table (using class name as table name)
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `Thread` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `note_id` INTEGER NOT NULL,
                `note_firebase_id` TEXT,
                `title` TEXT NOT NULL,
                `date` INTEGER NOT NULL,
                `last_updated` INTEGER NOT NULL,
                `firebase_id` TEXT,
                `is_synced` INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(`note_id`) REFERENCES `Note`(`id`) ON DELETE CASCADE
            )
        """
        )
        
        // Create messages table
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `messages` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `thread_id` INTEGER NOT NULL,
                `role` TEXT NOT NULL,
                `content` TEXT NOT NULL,
                `timestamp` INTEGER NOT NULL,
                `message_type` TEXT NOT NULL DEFAULT 'TEXT',
                `firebase_id` TEXT,
                `is_synced` INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(`thread_id`) REFERENCES `Thread`(`id`) ON DELETE CASCADE
            )
        """
        )
        
        // Create indexes for better performance
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_Thread_note_id` ON `Thread` (`note_id`)")
        database.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_thread_id` ON `messages` (`thread_id`)")
    }
}

val appModule = module {
    single {
        HttpClient(CIO) {
            install(HttpTimeout) {
                requestTimeoutMillis = 60000 // 60 seconds
                connectTimeoutMillis = 30000 // 30 seconds
                socketTimeoutMillis = 60000  // 60 seconds
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
    single<ChatService> {
        Ktorfit.Builder()
//            .baseUrl("http://192.168.1.226:8000/")
            .baseUrl("https://api.ai.digitalnext.business/")
            .httpClient(get<HttpClient>())
            .build()
            .createChatService()
    }

    single { get<Context>().dataStore }
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single { ThemeRepository(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { EmotionViewModel(get()) }
    viewModel { (modelName: String) -> NotesViewModel(get(), get(), get(named(modelName)), get()) }
    viewModel { (modelName: String) -> QuestionViewModel(get(), get(), get(named(modelName))) }
    viewModel { (modelName: String) -> ChatViewModel(get(named(modelName)), get(), get(), get()) }
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "note-list"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
         .fallbackToDestructiveMigration() // Remove this after successful migration
         .build()
    }
    single { NoteRepository(get<AppDatabase>().noteDao()) }
    single { QuestionRepository(get<AppDatabase>().questionDao()) }
    single { ThreadRepository(get<AppDatabase>().threadDao()) }
    single { ProfileRepository(get()) }
    single { AuthRepository(get(), get()) }

    viewModel { ProfileViewModel(get()) }
    single { EmotionModel(get()) }
    single { EmotionRepository(get()) }
    viewModel { LoginViewModel(get()) }

    factory(named("Neuro")) { IntrospectionNeuroImpl(get(), get()) } bind IntrospectionRepository::class
}