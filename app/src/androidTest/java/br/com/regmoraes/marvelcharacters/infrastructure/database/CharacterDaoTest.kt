package br.com.regmoraes.marvelcharacters.infrastructure.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.regmoraes.marvelcharacters.CharacterStubs.characterOne
import br.com.regmoraes.marvelcharacters.CharacterStubs.characterTwo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {
    private lateinit var favoritesDao: CharacterDao
    private lateinit var db: MarvelCharactersDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MarvelCharactersDatabase::class.java
        ).build()
        favoritesDao = db.characterDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertCharactersAsFavoriteAndRead() = runBlocking {

        favoritesDao.insertAsFavorite(characterOne.toEntity())
        favoritesDao.insertAsFavorite(characterTwo.toEntity())

        val characters = favoritesDao.getAllFavorites().first()

        assertEquals(2, characters.size)
    }

    @Test
    @Throws(Exception::class)
    fun removeCharacterFromFavorite() = runBlocking {

        favoritesDao.insertAsFavorite(characterOne.toEntity())
        favoritesDao.insertAsFavorite(characterTwo.toEntity())

        favoritesDao.removeFavorite(characterOne.id)

        val characters = favoritesDao.getAllFavorites().first()

        assertEquals(1, characters.size)
    }

    @Test
    @Throws(Exception::class)
    fun abortCharacterInsertionInFavoriteIfAlreadyInserted() {

        favoritesDao.insertAsFavorite(characterOne.toEntity())

        val insertedEntityId = favoritesDao.insertAsFavorite(characterOne.toEntity())

        assertEquals(-1L, insertedEntityId)
    }
}