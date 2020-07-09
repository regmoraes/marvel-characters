package br.com.regmoraes.marvelcharacters.infrastructure.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

@Dao
interface SeriesDao {

    @Query("SELECT * FROM series WHERE characterId = :characterId")
    fun getSeries(characterId: Long): List<SeriesEntity>

    @Insert(onConflict = IGNORE)
    fun insertSeries(series: List<SeriesEntity>): List<Long>

    @Query("DELETE FROM series WHERE characterId == :characterId")
    fun removeSeries(characterId: Long): Int
}