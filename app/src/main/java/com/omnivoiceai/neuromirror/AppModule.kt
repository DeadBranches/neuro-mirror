package com.omnivoiceai.neuromirror

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.omnivoiceai.neuromirror.data.database.AppDatabase
import com.omnivoiceai.neuromirror.data.database.MIGRATION_1_2
import com.omnivoiceai.neuromirror.data.database.MIGRATION_2_3
import com.omnivoiceai.neuromirror.data.database.MIGRATION_3_4
import com.omnivoiceai.neuromirror.data.ml.EmotionModel
import com.omnivoiceai.neuromirror.data.remote.ChatDataSource
import com.omnivoiceai.neuromirror.data.remote.createChatDataSource
import com.omnivoiceai.neuromirror.data.repositories.AuthRepository
import com.omnivoiceai.neuromirror.data.repositories.BadgeRepository
import com.omnivoiceai.neuromirror.data.repositories.EmotionRepository
import com.omnivoiceai.neuromirror.data.repositories.IntrospectionNeuroImpl
import com.omnivoiceai.neuromirror.data.repositories.IntrospectionRepository
import com.omnivoiceai.neuromirror.data.repositories.LanguageRepository
import com.omnivoiceai.neuromirror.data.repositories.NoteRepository
import com.omnivoiceai.neuromirror.data.repositories.ProfileRepository
import com.omnivoiceai.neuromirror.data.repositories.QuestionRepository
import com.omnivoiceai.neuromirror.data.repositories.ThemeRepository
import com.omnivoiceai.neuromirror.data.repositories.ThreadRepository
import com.omnivoiceai.neuromirror.domain.model.Config
import com.omnivoiceai.neuromirror.ui.screens.auth.login.LoginViewModel
import com.omnivoiceai.neuromirror.ui.screens.auth.register.RegisterViewModel
import com.omnivoiceai.neuromirror.ui.screens.chat.ChatViewModel
import com.omnivoiceai.neuromirror.ui.screens.note_detail.EmotionViewModel
import com.omnivoiceai.neuromirror.ui.screens.notes.NotesViewModel
import com.omnivoiceai.neuromirror.ui.screens.profile.BadgeViewModel
import com.omnivoiceai.neuromirror.ui.screens.profile.ProfileViewModel
import com.omnivoiceai.neuromirror.ui.screens.questions.QuestionViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.language.LanguageViewModel
import com.omnivoiceai.neuromirror.ui.screens.settings.theme.ThemeViewModel
import com.omnivoiceai.neuromirror.worker.ExportImportWorker
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore(Config.PREFERENCE_DATA_STORE)


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
    single<ChatDataSource> {
        Ktorfit.Builder()
            .baseUrl(Config.BASE_URL)
            .httpClient(get<HttpClient>())
            .build()
            .createChatDataSource()
    }
    single { get<Context>().dataStore }
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            Config.DATABASE_NAME
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .build()
    }
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single { ThemeRepository(get()) }
    single { LanguageRepository(get())}
    viewModel { ThemeViewModel(get()) }
    viewModel { LanguageViewModel(get()) }
    viewModel { EmotionViewModel(get()) }
    viewModel { BadgeViewModel(get()) }
    viewModel { (modelName: String) -> NotesViewModel(get(), get(), get(named(modelName)), get(), get()) }
    viewModel { (modelName: String) -> QuestionViewModel(get(), get(), get(named(modelName)), get()) }
    viewModel { (modelName: String) -> ChatViewModel(get(named(modelName)), get(), get(), get(), get()) }
    single { NoteRepository(get<AppDatabase>().noteDao()) }
    single { QuestionRepository(get<AppDatabase>().questionDao()) }
    single { ThreadRepository(get<AppDatabase>().threadDao()) }
    single { ProfileRepository(get()) }
    single { BadgeRepository(get<AppDatabase>().badgeDao()) }
    single { AuthRepository(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    single { EmotionModel(get()) }
    single { EmotionRepository(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    workerOf(::ExportImportWorker)
    factory(named("Neuro")) { IntrospectionNeuroImpl(get(), get()) } bind IntrospectionRepository::class
}