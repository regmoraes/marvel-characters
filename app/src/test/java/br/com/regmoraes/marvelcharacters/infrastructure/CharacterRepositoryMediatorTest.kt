package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_LIMIT
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_OFFSET
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.api.RetrofitConfiguration
import br.com.regmoraes.marvelcharacters.infrastructure.database.CharacterDao
import br.com.regmoraes.marvelcharacters.infrastructure.database.toEntity
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterTwo
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterRepositoryMediatorTest {
    private val webServerMock = MockWebServer()
    private lateinit var characterDao: CharacterDao
    private lateinit var characterRestService: CharacterRestService
    private lateinit var favoriteStatusSynchronizer: FavoriteStatusSynchronizer
    private lateinit var characterRepositoryMediator: CharacterRepositoryMediator

    @BeforeAll
    fun setUp() {
        webServerMock.start()
        val retrofit = RetrofitConfiguration.build(webServerMock.url("/").toString())

        characterDao = mockk()
        characterRestService = retrofit.create(CharacterRestService::class.java)
        favoriteStatusSynchronizer = FavoriteStatusSynchronizer()
        characterRepositoryMediator =
            CharacterRepositoryMediator(
                characterDao,
                characterRestService,
                favoriteStatusSynchronizer
            )
    }

    @AfterAll
    fun tearDown() {
        webServerMock.shutdown()
    }

    @Nested
    @DisplayName("Given a Marvel Characters API")
    inner class MarvelCharactersApi {

        @Test
        fun `When fetching character And response is successful Then return a list of characters`() =
            runBlocking {

                val jsonBody =
                    FileUtils.readTestResourceFile(
                        "characters/api/characters-response.json"
                    )

                val response = MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Cache-Control", "no-cache")
                    .setBody(jsonBody)

                webServerMock.enqueue(response)

                val event = characterRepositoryMediator.getCharacters(0, 20)

                assert(event is Event.Success && event.data.isNotEmpty())
            }

        @Test
        fun `When response is unsuccessful Then it should return a fetch error`() = runBlocking {

            val response = MockResponse()
                .setResponseCode(500)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")

            webServerMock.enqueue(response)

            val event =
                characterRepositoryMediator.getCharacters(DEFAULT_OFFSET, DEFAULT_LIMIT)

            assert(event is Event.Error)
        }
    }

    @Nested
    @DisplayName("Given a database with no favorites")
    inner class EmptyDatabase {

        @Test
        @Throws(Exception::class)
        fun `When insert returns new entity id Then it should return favorite added`() =
            runBlocking {

                coEvery { characterDao.insertAsFavorite(eq(characterOne.toEntity())) } returns characterOne.id

                val event = characterRepositoryMediator.insertFavorite(characterOne)

                assert(event is Event.Success && event.data)
            }

        @Test
        @Throws(Exception::class)
        fun `When insert do not return new id Then it should return an error`() = runBlocking {

            coEvery { characterDao.insertAsFavorite(eq(characterOne.toEntity())) } returns -1L

            val event = characterRepositoryMediator.insertFavorite(characterOne)

            assert(event is Event.Error)
        }
    }

    @Nested
    @DisplayName("Given a database with favorites inserted")
    inner class NonEmptyDatabase {

        @Test
        @Throws(Exception::class)
        fun `When requesting favorites Then it should return list of favorites characters`() =
            runBlocking {

                val favorites = listOf(
                    characterOne.copy(isFavorite = true),
                    characterTwo.copy(isFavorite = true)
                )
                val favoritesEntities = favorites.map { it.toEntity() }

                every { characterDao.getAllFavorites() } returns flowOf(favoritesEntities)

                characterRepositoryMediator.getFavorites().collect { event ->
                    assert(event is Event.Success && event.data == favorites)
                }
            }

        @Test
        @Throws(Exception::class)
        fun `When removing a favorite Then it should return favorite removed`() = runBlocking {

            coEvery { characterDao.removeFavorite(characterOne.id) } returns 1

            val event = characterRepositoryMediator.removeFavorite(characterOne)

            assert(event is Event.Success && event.data)
        }
    }
}