package com.omnivoiceai.neuromirror.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
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
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create Thread table (using class name as table name)
        db.execSQL(
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
        db.execSQL(
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
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Thread_note_id` ON `Thread` (`note_id`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_messages_thread_id` ON `messages` (`thread_id`)")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `Badge` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `badgeKey` TEXT NOT NULL,
                `category` TEXT NOT NULL,
                `level` INTEGER NOT NULL,
                `isUnlocked` INTEGER NOT NULL,
                `unlockTimestamp` INTEGER
            )
            """
        )
    }
}
