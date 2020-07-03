package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ChangeFavoriteStatusTest {

    private lateinit var characterRepository: CharacterRepository
    private lateinit var changeFavoriteStatus: ChangeFavoriteStatus

    @BeforeAll
    fun setUp() {
        characterRepository = mockk()
        changeFavoriteStatus = ChangeFavoriteStatus(characterRepository)
    }

    @Nested
    @DisplayName("Given a character that is not a favorite")
    inner class NonFavoriteCharacter {

        private val notFavorite = characterOne

        @Test
        fun `When changing favorite status Then it should change to favorite and return favorite added`() =
            runBlocking {

                val favorite = notFavorite.copy(isFavorite = true)

                coEvery { characterRepository.insertFavorite(any()) } returns FavoritesEvent.FavoriteAdded(
                    favorite
                )

                val event = changeFavoriteStatus.execute(notFavorite)

                assert(event is FavoritesEvent.FavoriteAdded && event.addedCharacter == favorite)
            }
    }

    @Nested
    @DisplayName("Given a character that is a favorite")
    inner class FavoriteCharacter {

        private val favorite = characterOne.copy(isFavorite = true)

        @Test
        fun `When changing favorite status Then it should change to not favorite and return favorite removed`() =
            runBlocking {

                val notFavorite = favorite.copy(isFavorite = false)

                coEvery { characterRepository.removeFavorite(any()) } returns FavoritesEvent.FavoriteRemoved(
                    notFavorite
                )

                val event = changeFavoriteStatus.execute(favorite)

                assert(event is FavoritesEvent.FavoriteRemoved && event.removedCharacter == notFavorite)
            }
    }
}