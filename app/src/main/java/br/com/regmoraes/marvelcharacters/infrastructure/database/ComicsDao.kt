package br.com.regmoraes.marvelcharacters.infrastructure.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

@Dao
interface ComicsDao {

    @Query("SELECT * FROM comics WHERE characterId = :characterId")
    fun getComics(characterId: Long): List<ComicEntity>

    @Insert(onConflict = IGNORE)
    fun insertComics(comics: List<ComicEntity>): List<Long>

    @Query("DELETE FROM comics WHERE characterId == :characterId")
    fun removeComics(characterId: Long): Int
}