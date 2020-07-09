package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.ComicRepository
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.comicOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.comicTwo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FetchComicsTest {

    @Nested
    @DisplayName("Given a FetchComics request")
    inner class FetchComicsRequest {

        private lateinit var fetchComics: FetchComics
        private lateinit var comicRepository: ComicRepository

        @BeforeEach
        fun setUp() {
            comicRepository = mockk()
            fetchComics = FetchComics(comicRepository)
        }

        @Test
        fun `When request is successful Then it should return comics fetched`() = runBlocking {

            val comics = listOf(comicOne, comicTwo)

            coEvery { comicRepository.getComics(any()) } returns Event.success(comics)

            val event = fetchComics.execute(characterOne.id)

            assert(event is Event.Success && event.data.isNotEmpty())
        }

        @Test
        fun `When request has error Then it should return fetch error`() = runBlocking {

            coEvery { comicRepository.getComics(any()) } returns Event.error(IllegalStateException("An error"))

            val event = fetchComics.execute(characterOne.id)

            assert(event is Event.Error)
        }
    }
}