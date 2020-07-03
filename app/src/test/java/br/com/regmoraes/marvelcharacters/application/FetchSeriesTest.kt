package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.seriesOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.seriesTwo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FetchSeriesTest {

    @Nested
    @DisplayName("Given a FetchSeries request")
    inner class FetchSeriesRequest {

        private lateinit var fetchSeries: FetchSeries
        private lateinit var characterRepository: CharacterRepository

        @BeforeEach
        fun setUp() {
            characterRepository = mockk()
            fetchSeries = FetchSeries(characterRepository)
        }

        @Test
        fun `When request is successful Then it should return series fetched`() = runBlocking {

            val series = listOf(seriesOne, seriesTwo)

            coEvery { characterRepository.getSeries(any()) } returns CharacterEvent.SeriesFetched(
                series
            )

            val event = fetchSeries.execute(characterOne.id)

            assert(event is CharacterEvent.SeriesFetched && event.series.isNotEmpty())
        }

        @Test
        fun `When request has error Then it should return fetch error`() = runBlocking {

            coEvery { characterRepository.getSeries(any()) } returns CharacterEvent.FetchError(
                IllegalStateException("An error")
            )

            val event = fetchSeries.execute(characterOne.id)

            assertTrue(event is CharacterEvent.FetchError)
        }
    }
}