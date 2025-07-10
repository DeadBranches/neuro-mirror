package com.omnivoiceai.neuromirror.data.database.badge

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDAO {
    @Query("SELECT * FROM Badge")
    fun getAll(): Flow<List<Badge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(badge: Badge)

    @Update
    suspend fun update(badge: Badge)

    @Query("SELECT * FROM Badge WHERE badgeKey = :key")
    suspend fun getByKey(key: String): Badge?
}
