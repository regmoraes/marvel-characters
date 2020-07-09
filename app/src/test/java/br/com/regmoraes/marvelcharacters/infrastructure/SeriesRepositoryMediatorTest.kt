package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.api.RetrofitConfiguration
import br.com.regmoraes.marvelcharacters.infrastructure.database.SeriesDao
import br.com.regmoraes.marvelcharacters.infrastructure.database.toEntity
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.seriesOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.seriesTwo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SeriesRepositoryMediatorTest {
    private val webServerMock = MockWebServer()
    private lateinit var seriesDao: SeriesDao
    private lateinit var characterRestService: CharacterRestService
    private lateinit var seriesRepositoryMediator: SeriesRepositoryMediator

    @BeforeAll
    fun setUp() {
        webServerMock.start()
        val retrofit = RetrofitConfiguration.build(webServerMock.url("/").toString())

        seriesDao = mockk()
        characterRestService = retrofit.create(CharacterRestService::class.java)
        seriesRepositoryMediator = SeriesRepositoryMediator(seriesDao, characterRestService)
    }

    @AfterAll
    fun tearDown() {
        webServerMock.shutdown()
    }

    @Nested
    @DisplayName("Given a database with no series saved")
    inner class EmptyDatabase {

        @Test
        @Throws(Exception::class)
        fun `When insert returns correct insertion count Then it should return series added`() =
            runBlocking {

                val character = characterOne
                val series = listOf(seriesOne, seriesTwo)
                coEvery { seriesDao.insertSeries(any()) } returns series.map { it.id }

                val event = seriesRepositoryMediator.insertSeries(character.id, series)

                assertTrue(event is Event.Success)
            }

        @Test
        @Throws(Exception::class)
        fun `When insert do not return correct insertion count Then it should return false`() =
            runBlocking {

                val character = characterOne
                val series = listOf(seriesOne, seriesTwo)
                coEvery { seriesDao.insertSeries(any()) } returns series.dropLast(1).map { it.id }

                val event = seriesRepositoryMediator.insertSeries(character.id, series)

                assert(event is Event.Success && !event.data)
            }

        @Test
        @Throws(Exception::class)
        fun `When requesting series Then it should fetch series from remote API`() =
            runBlocking {

                val jsonBody =
                    FileUtils.readTestResourceFile(
                        "characters/api/series-response.json"
                    )

                val response = MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Cache-Control", "no-cache")
                    .setBody(jsonBody)

                webServerMock.enqueue(response)

                val character = characterOne

                coEvery { seriesDao.getSeries(character.id) } returns emptyList()

                val event = seriesRepositoryMediator.getSeries(character.id)

                assertTrue(event is Event.Success && event.data.isNotEmpty())
            }
    }

    @Nested
    @DisplayName("Given a database with series inserted")
    inner class NonEmptyDatabase {

        @Test
        @Throws(Exception::class)
        fun `When requesting series Then it should return series from local`() =
            runBlocking {

                val character = characterOne
                val series = listOf(seriesOne, seriesTwo)
                val seriesEntities = series.map { it.toEntity(character.id) }

                coEvery { seriesDao.getSeries(character.id) } returns seriesEntities

                val event = seriesRepositoryMediator.getSeries(character.id)

                assertTrue(event is Event.Success && event.data == series)
            }
    }
}