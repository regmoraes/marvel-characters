package br.com.regmoraes.marvelcharacters.infrastructure.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.seriesOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.seriesTwo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SeriesDaoTest {
    private lateinit var seriesDao: SeriesDao
    private lateinit var db: MarvelCharactersDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MarvelCharactersDatabase::class.java
        ).build()
        seriesDao = db.seriesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertSeriesAndRead() = runBlocking {

        val character = characterOne
        val seriesEntities = listOf(seriesOne, seriesTwo).map { it.toEntity(character.id) }

        seriesDao.insertSeries(seriesEntities)

        val characters = seriesDao.getSeries(character.id)

        assertEquals(2, characters.size)
    }

    @Test
    @Throws(Exception::class)
    fun removeSeries() = runBlocking {

        val character = characterOne
        val seriesEntities = listOf(seriesOne, seriesTwo).map { it.toEntity(character.id) }

        seriesDao.insertSeries(seriesEntities)
        seriesDao.removeSeries(characterOne.id)

        val series = seriesDao.getSeries(character.id)

        assertEquals(0, series.size)
    }
}