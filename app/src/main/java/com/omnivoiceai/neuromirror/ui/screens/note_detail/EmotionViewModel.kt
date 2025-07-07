package com.omnivoiceai.neuromirror.ui.screens.note_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.data.repositories.EmotionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmotionViewModel(
    private val repository: EmotionRepository
) : ViewModel() {

    private val _emotion = MutableStateFlow<EmotionDetected?>(null)
    val emotion = _emotion.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun detectEmotion(text: String) {
        viewModelScope.launch {
            _emotion.value = repository.classify(text)
        }
    }
}
