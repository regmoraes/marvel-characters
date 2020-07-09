package br.com.regmoraes.marvelcharacters.infrastructure.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters")
    fun getAllFavorites(): Flow<List<CharacterEntity>>

    @Insert(onConflict = IGNORE)
    fun insertAsFavorite(characterEntity: CharacterEntity): Long

    @Query("DELETE FROM characters WHERE id == :characterId")
    fun removeFavorite(characterId: Long): Int
}