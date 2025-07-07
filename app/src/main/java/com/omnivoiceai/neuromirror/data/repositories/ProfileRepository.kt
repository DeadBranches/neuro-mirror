package com.omnivoiceai.neuromirror.data.repositories

import androidx.credentials.CustomCredential
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.flow.map

class ProfileRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val FIRST_NAME_KEY = stringPreferencesKey("firstname")
        private val LAST_NAME_KEY = stringPreferencesKey("lastname")
    }

    val username = dataStore.data.map { it[USERNAME_KEY] ?: "" }
    val firstName = dataStore.data.map { it[FIRST_NAME_KEY] ?: "" }
    val lastName = dataStore.data.map { it[LAST_NAME_KEY] ?: "" }

    suspend fun setUsername(username: String) = dataStore.edit { it[USERNAME_KEY] = username }
    suspend fun setFirstName(firstName: String) = dataStore.edit { it[FIRST_NAME_KEY] = firstName }
    suspend fun setLastName(lastName: String) = dataStore.edit { it[LAST_NAME_KEY] = lastName }

}
