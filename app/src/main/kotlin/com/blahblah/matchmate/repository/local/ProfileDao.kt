package com.blahblah.matchmate.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blahblah.matchmate.models.match.MatchStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY createdAt ASC, id ASC")
    fun observeAll(): Flow<List<ProfileEntity>>

    @Query("SELECT COUNT(*) FROM profiles")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM profiles WHERE status = :status")
    suspend fun countByStatus(status: MatchStatus): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<ProfileEntity>)

    @Query("UPDATE profiles SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: MatchStatus)
}
