package br.com.regmoraes.marvelcharacters.infrastructure.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.comicOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.comicTwo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ComicsDaoTest {
    private lateinit var comicsDao: ComicsDao
    private lateinit var db: MarvelCharactersDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MarvelCharactersDatabase::class.java
        ).build()
        comicsDao = db.comicsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertComicsAndRead() = runBlocking {

        val character = characterOne
        val comicEntities = listOf(comicOne, comicTwo).map { it.toEntity(character.id) }

        comicsDao.insertComics(comicEntities)

        val characters = comicsDao.getComics(character.id)

        assertEquals(2, characters.size)
    }

    @Test
    @Throws(Exception::class)
    fun removeComics() = runBlocking {

        val character = characterOne
        val comicEntities = listOf(comicOne, comicTwo).map { it.toEntity(character.id) }

        comicsDao.insertComics(comicEntities)
        comicsDao.removeComics(characterOne.id)

        val comics = comicsDao.getComics(character.id)

        assertEquals(0, comics.size)
    }
}