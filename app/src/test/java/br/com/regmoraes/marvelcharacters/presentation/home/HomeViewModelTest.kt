package br.com.regmoraes.marvelcharacters.presentation.home

import br.com.regmoraes.marvelcharacters.application.*
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_LIMIT
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_OFFSET
import br.com.regmoraes.marvelcharacters.extension.CoroutinesTestExtension
import br.com.regmoraes.marvelcharacters.extension.InstantExecutorExtension
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterTwo
import br.com.regmoraes.marvelcharacters.presentation.testObserver
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class HomeViewModelTest {

    private lateinit var changeFavoriteStatus: ChangeFavoriteStatus
    private lateinit var fetchFavorites: FetchFavorites
    private lateinit var fetchCharacters: FetchCharacters

    private lateinit var homeViewModel: HomeViewModel

    @BeforeEach
    fun setUp() {
        fetchFavorites = mockk()
        fetchCharacters = mockk()
        changeFavoriteStatus = mockk()

        homeViewModel = HomeViewModel(changeFavoriteStatus, fetchFavorites, fetchCharacters)
    }

    @Nested
    @DisplayName("Given a fetch characters request")
    inner class FetchCharactersRequest {

        @Test
        fun `When it is made Then it should emit fetching and fetched character if successful`() =
            runBlocking {

                val characters = listOf(characterOne, characterTwo)
                coEvery { fetchCharacters.execute() } returns CharacterEvent.CharactersFetched(
                    characters
                )

                val eventsObserver = homeViewModel.characterEvents.testObserver()

                val expectedEvents = listOf(
                    CharacterEvent.FetchingCharacters,
                    CharacterEvent.CharactersFetched(characters)
                )

                homeViewModel.fetchCharacters(DEFAULT_OFFSET, DEFAULT_LIMIT)

                assertEquals(expectedEvents, eventsObserver.observedValues)
            }

        @Test
        fun `When it is made Then it should emit fetching and fetched error if it has error`() =
            runBlocking {

                val error = IllegalStateException("Some error")
                coEvery { fetchCharacters.execute() } returns CharacterEvent.FetchError(error)

                val eventsObserver = homeViewModel.characterEvents.testObserver()

                homeViewModel.fetchCharacters(DEFAULT_OFFSET, DEFAULT_LIMIT)

                assertEquals(eventsObserver.observedValues, eventsObserver.observedValues)
            }
    }

    @Nested
    @DisplayName("Given a character that is a favorite")
    inner class FavoriteCharacter {

        private val favorite = characterOne.copy(isFavorite = true)

        @Test
        fun `When user change its status Then it should emit character removed`() =
            runBlocking {

                val notFavorite = favorite.copy(isFavorite = false)
                coEvery { changeFavoriteStatus.execute(favorite) } returns FavoritesEvent.FavoriteRemoved(
                    notFavorite
                )

                val eventsObserver = homeViewModel.favoritesEvents.testObserver()

                val expectedEvents = listOf(FavoritesEvent.FavoriteRemoved(notFavorite))

                homeViewModel.changeFavoriteStatus(favorite)

                assertEquals(expectedEvents, eventsObserver.observedValues)
            }
    }

    @Nested
    @DisplayName("Given a character that is not a favorite")
    inner class NotFavoriteCharacter {

        private val notFavorite = characterOne.copy(isFavorite = false)

        @Test
        fun `When user add it to favorite Then it should emit character added`() =
            runBlocking {

                val favorite = notFavorite.copy(isFavorite = true)
                coEvery { changeFavoriteStatus.execute(notFavorite) } returns FavoritesEvent.FavoriteAdded(
                    favorite
                )

                val eventsObserver = homeViewModel.favoritesEvents.testObserver()

                val expectedEvents = listOf(FavoritesEvent.FavoriteAdded(favorite))

                homeViewModel.changeFavoriteStatus(notFavorite)

                assertEquals(expectedEvents, eventsObserver.observedValues)
            }

    }
}