package com.omnivoiceai.neuromirror.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omnivoiceai.neuromirror.data.database.badge.BadgeCategory
import com.omnivoiceai.neuromirror.data.repositories.BadgeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BadgeViewModel(
    private val repository: BadgeRepository
): ViewModel() {
    val badges = repository.allBadges.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun unlockBadge(key: String, category: BadgeCategory, level: Int) {
        viewModelScope.launch {
            repository.unlockBadgeIfNeeded(key, category, level)
        }
    }
}
