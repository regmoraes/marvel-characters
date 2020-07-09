package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AddToFavoriteTest {

    private lateinit var characterRepository: CharacterRepository
    private lateinit var addToFavorite: AddToFavorite

    @BeforeAll
    fun setUp() {
        characterRepository = mockk()
        addToFavorite = AddToFavorite(characterRepository)
    }

    @Nested
    @DisplayName("Given a character that is not a favorite")
    inner class NonFavoriteCharacter {

        private val notFavorite = characterOne

        @Test
        fun `When adding to favorite Then it should change to favorite insert on repository and return it`() =
            runBlocking {

                val favorite = notFavorite.copy(isFavorite = true)

                coEvery { characterRepository.insertFavorite(any()) } returns Event.success(true)

                val event = addToFavorite.execute(notFavorite)

                assert(event is Event.Success && event.data == favorite)
            }
    }
}