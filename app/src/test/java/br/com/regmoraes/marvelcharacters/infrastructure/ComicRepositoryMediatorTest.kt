package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.api.RetrofitConfiguration
import br.com.regmoraes.marvelcharacters.infrastructure.database.ComicsDao
import br.com.regmoraes.marvelcharacters.infrastructure.database.toEntity
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.comicOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.comicTwo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComicRepositoryMediatorTest {
    private val webServerMock = MockWebServer()
    private lateinit var comicsDao: ComicsDao
    private lateinit var characterRestService: CharacterRestService
    private lateinit var comicRepositoryMediator: ComicRepositoryMediator

    @BeforeAll
    fun setUp() {
        webServerMock.start()
        val retrofit = RetrofitConfiguration.build(webServerMock.url("/").toString())

        comicsDao = mockk()
        characterRestService = retrofit.create(CharacterRestService::class.java)
        comicRepositoryMediator = ComicRepositoryMediator(comicsDao, characterRestService)
    }

    @AfterAll
    fun tearDown() {
        webServerMock.shutdown()
    }

    @Nested
    @DisplayName("Given a database with no comics saved")
    inner class EmptyDatabase {

        @Test
        @Throws(Exception::class)
        fun `When insert returns correct insertion count Then it should return comics added`() =
            runBlocking {

                val character = characterOne
                val comics = listOf(comicOne, comicTwo)
                coEvery { comicsDao.insertComics(any()) } returns comics.map { it.id }

                val event = comicRepositoryMediator.insertComics(character.id, comics)

                assertTrue(event is Event.Success)
            }

        @Test
        @Throws(Exception::class)
        fun `When insert do not return correct insertion count Then it should return an error`() =
            runBlocking {

                val character = characterOne
                val comics = listOf(comicOne, comicTwo)
                coEvery { comicsDao.insertComics(any()) } returns comics.dropLast(1).map { it.id }

                val event = comicRepositoryMediator.insertComics(character.id, comics)

                assertTrue(event is Event.Error)
            }

        @Test
        @Throws(Exception::class)
        fun `When requesting comics Then it should fetch comics from remote API`() =
            runBlocking {

                val jsonBody =
                    FileUtils.readTestResourceFile(
                        "characters/api/comics-response.json"
                    )

                val response = MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Cache-Control", "no-cache")
                    .setBody(jsonBody)

                webServerMock.enqueue(response)

                val character = characterOne

                coEvery { comicsDao.getComics(character.id) } returns emptyList()

                val event = comicRepositoryMediator.getComics(character.id)

                assertTrue(event is Event.Success && event.data.isNotEmpty())
            }
    }

    @Nested
    @DisplayName("Given a database with comics inserted")
    inner class NonEmptyDatabase {

        @Test
        @Throws(Exception::class)
        fun `When requesting comics Then it should return comics from local`() =
            runBlocking {

                val character = characterOne
                val comics = listOf(comicOne, comicTwo)
                val comicsEntities = comics.map { it.toEntity(character.id) }

                coEvery { comicsDao.getComics(character.id) } returns comicsEntities

                val event = comicRepositoryMediator.getComics(character.id)

                assertTrue(event is Event.Success && event.data == comics)
            }
    }
}