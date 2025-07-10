package com.omnivoiceai.neuromirror.data.repositories

import com.omnivoiceai.neuromirror.data.database.badge.Badge
import com.omnivoiceai.neuromirror.data.database.badge.BadgeCategory
import com.omnivoiceai.neuromirror.data.database.badge.BadgeDAO
import com.omnivoiceai.neuromirror.data.database.note.EmotionDetected
import com.omnivoiceai.neuromirror.data.database.note.toEmoji
import com.omnivoiceai.neuromirror.utils.Logger
import com.omnivoiceai.neuromirror.utils.UiEventBus



class BadgeRepository(private val dao: BadgeDAO) {
    val allBadges = dao.getAll()

    suspend fun unlockBadgeIfNeeded(
        key: String,
        category: BadgeCategory,
        level: Int,
        emojy: String? = null
    ){
        val badge = dao.getByKey(key)
        Logger.d("Tentativo sblocco badge: $key - unlocked=${badge?.isUnlocked}")
        if (badge == null || !badge.isUnlocked) {
            val unlockedBadge = Badge(
                badgeKey = key,
                category = category,
                level = level,
                isUnlocked = true,
                unlockTimestamp = System.currentTimeMillis()
            )
            dao.insert(unlockedBadge)
            UiEventBus.showBadge(badge = unlockedBadge, emojy)
        }
    }

    private suspend fun checkMilestone(
        category: BadgeCategory,
        count: Int,
        suffix: String = "",
        emoji: String? = null
    ) {
        val milestones = listOf(1, 3, 5, 10)

        milestones.find { it == count }?.let { milestone ->
            val key = if (suffix.isBlank()) {
                "${category.name.lowercase()}_$milestone"
            } else {
                "${category.name.lowercase()}_${suffix.lowercase()}_$milestone"
            }
            val level = milestone
            unlockBadgeIfNeeded(key, category, level, emoji)
        }
    }

    suspend fun checkNoteMilestones(noteCount: Int) {
        checkMilestone(BadgeCategory.NOTE, noteCount)
    }


    suspend fun checkEmotionMilestones(emotion: EmotionDetected, emotionCount: Int) {
        checkMilestone(
            category = BadgeCategory.EMOTION,
            count = emotionCount,
            suffix = emotion.name,
            emoji = emotion.toEmoji()
        )
    }

    suspend fun checkQuestionMilestones(questionCount: Int) {
        checkMilestone(BadgeCategory.QUESTION, questionCount)
    }

    suspend fun checkMessageMilestones(messageCount: Int) {
        checkMilestone(BadgeCategory.MESSAGE, messageCount)
    }
}