package myplayground.example.dicodingstory.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import myplayground.example.dicodingstory.model.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(story: List<Story>)

    @Query("SELECT * FROM story")
    fun fetchAll(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}