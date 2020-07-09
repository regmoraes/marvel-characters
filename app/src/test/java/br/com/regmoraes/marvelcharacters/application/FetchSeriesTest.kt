package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.SeriesRepository
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.seriesOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.seriesTwo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FetchSeriesTest {

    @Nested
    @DisplayName("Given a FetchSeries request")
    inner class FetchSeriesRequest {

        private lateinit var fetchSeries: FetchSeries
        private lateinit var seriesRepository: SeriesRepository

        @BeforeEach
        fun setUp() {
            seriesRepository = mockk()
            fetchSeries = FetchSeries(seriesRepository)
        }

        @Test
        fun `When request is successful Then it should return series fetched`() = runBlocking {

            val series = listOf(seriesOne, seriesTwo)

            coEvery { seriesRepository.getSeries(any()) } returns Event.success(series)

            val event = fetchSeries.execute(characterOne.id)

            assert(event is Event.Success && event.data.isNotEmpty())
        }

        @Test
        fun `When request has error Then it should return fetch error`() = runBlocking {

            coEvery { seriesRepository.getSeries(any()) } returns Event.error(
                IllegalStateException("An error")
            )

            val event = fetchSeries.execute(characterOne.id)

            assert(event is Event.Error)
        }
    }
}