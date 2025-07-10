package com.omnivoiceai.neuromirror.data.database.badge

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class BadgeCategory {
    NOTE,
    EMOTION,
    QUESTION,
    MESSAGE
}

@Entity
data class Badge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val badgeKey: String,
    val category: BadgeCategory,
    val level: Int,
    val isUnlocked: Boolean = false,
    val unlockTimestamp: Long? = null
)
