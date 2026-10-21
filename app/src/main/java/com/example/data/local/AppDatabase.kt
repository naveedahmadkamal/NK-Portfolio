package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "inquiries")
data class InquiryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val subject: String,
    val projectType: String,
    val timeline: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Saved"
)

@Entity(tableName = "case_study_bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val caseStudyId: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Dao
interface InquiryDao {
    @Query("SELECT * FROM inquiries ORDER BY timestamp DESC")
    fun getAllInquiries(): Flow<List<InquiryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: InquiryEntity): Long

    @Query("DELETE FROM inquiries WHERE id = :id")
    suspend fun deleteInquiry(id: Long)

    @Query("SELECT COUNT(*) FROM inquiries")
    fun getInquiryCount(): Flow<Int>
}

@Dao
interface BookmarkDao {
    @Query("SELECT caseStudyId FROM case_study_bookmarks")
    fun getAllBookmarks(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM case_study_bookmarks WHERE caseStudyId = :caseStudyId")
    suspend fun removeBookmark(caseStudyId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM case_study_bookmarks WHERE caseStudyId = :caseStudyId)")
    fun isBookmarked(caseStudyId: String): Flow<Boolean>
}

@Database(
    entities = [InquiryEntity::class, BookmarkEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inquiryDao(): InquiryDao
    abstract fun bookmarkDao(): BookmarkDao
}
