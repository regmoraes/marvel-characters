package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RemoveFromFavoriteTest {

    private lateinit var characterRepository: CharacterRepository
    private lateinit var removeFromFavorite: RemoveFromFavorite

    @BeforeAll
    fun setUp() {
        characterRepository = mockk()
        removeFromFavorite = RemoveFromFavorite(characterRepository)
    }

    @Nested
    @DisplayName("Given a character that is a favorite")
    inner class FavoriteCharacter {

        private val favorite = characterOne.copy(isFavorite = true)

        @Test
        fun `When removing from favorite Then it should change to not favorite remove from repository and return it`() =
            runBlocking {

                val notFavorite = favorite.copy(isFavorite = false)

                coEvery { characterRepository.removeFavorite(any()) } returns Event.success(true)

                val event = removeFromFavorite.execute(favorite)

                assert(event is Event.Success && event.data == notFavorite)
            }
    }
}