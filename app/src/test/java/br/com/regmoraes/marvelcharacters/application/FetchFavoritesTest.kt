package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.application.Event.Companion.success
import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterTwo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FetchFavoritesTest {

    @Nested
    @DisplayName("Given a FetchFavorites request")
    inner class FetchFavoritesRequest {

        private lateinit var fetchFavorites: FetchFavorites
        private lateinit var characterRepository: CharacterRepository

        @BeforeEach
        fun setUp() {
            characterRepository = mockk()
            fetchFavorites = FetchFavorites(characterRepository)
        }

        @Test
        fun `When request is successful Then it should return comics fetched`() = runBlocking {

            val favorites = listOf(characterOne, characterTwo)

            coEvery { characterRepository.getFavorites() } returns flowOf(success(favorites))

            fetchFavorites.execute().collect {
                assert(it is Event.Success && it.data.isNotEmpty())
            }
        }
    }
}