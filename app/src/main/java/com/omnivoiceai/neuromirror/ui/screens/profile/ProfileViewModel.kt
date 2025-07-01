package com.omnivoiceai.neuromirror.ui.screens.profile

import com.omnivoiceai.neuromirror.data.repositories.ProfileRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProfileState(val username: String, val firstName: String, val lastName: String)

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState("", "", ""))
        private set

    fun setUsername(username: String) {
        state = ProfileState(username, state.firstName, state.lastName)
        viewModelScope.launch {
            repository.setUsername(username)
        }
    }
    fun setFirstName(firstName: String) {
        state = ProfileState(state.username, firstName, state.lastName)
        viewModelScope.launch {
            repository.setFirstName(firstName)
        }
    }
    fun setLastName(lastName: String) {
        state = ProfileState(state.username, state.firstName, lastName)
        viewModelScope.launch {
            repository.setLastName(lastName)
        }
    }

    init {
        viewModelScope.launch {
            state = ProfileState(repository.username.first(), repository.firstName.first(), repository.lastName.first())
        }
    }
}
