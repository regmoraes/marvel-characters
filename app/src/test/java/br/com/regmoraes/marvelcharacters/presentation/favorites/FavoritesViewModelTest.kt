package br.com.regmoraes.marvelcharacters.presentation.favorites

import br.com.regmoraes.marvelcharacters.application.*
import br.com.regmoraes.marvelcharacters.application.Event.Companion.success
import br.com.regmoraes.marvelcharacters.extension.CoroutinesTestExtension
import br.com.regmoraes.marvelcharacters.extension.InstantExecutorExtension
import br.com.regmoraes.marvelcharacters.model.ModelStubs
import br.com.regmoraes.marvelcharacters.presentation.CoroutineContextProvider
import br.com.regmoraes.marvelcharacters.presentation.testObserver
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(InstantExecutorExtension::class)
@ExperimentalCoroutinesApi
internal class FavoritesViewModelTest {

    private lateinit var addToFavorite: AddToFavorite
    private lateinit var removeFromFavorite: RemoveFromFavorite
    private lateinit var fetchFavorites: FetchFavorites
    private lateinit var fetchAndSaveComicsAndSeries: FetchAndSaveComicsAndSeries
    private lateinit var saveComics: SaveComics
    private lateinit var saveSeries: SaveSeries

    private lateinit var favoritesViewModel: FavoritesViewModel

    @JvmField
    @RegisterExtension
    val coroutinesTestExtension = CoroutinesTestExtension()

    @BeforeEach
    fun setUp() {
        addToFavorite = mockk()
        removeFromFavorite = mockk()
        fetchFavorites = mockk()
        fetchAndSaveComicsAndSeries = mockk()
        saveComics = mockk()
        saveSeries = mockk()
        favoritesViewModel = FavoritesViewModel(
            addToFavorite,
            removeFromFavorite,
            fetchFavorites,
            fetchAndSaveComicsAndSeries,
            saveComics,
            saveSeries,
            CoroutineContextProvider(
                main = coroutinesTestExtension.dispatcher,
                io = coroutinesTestExtension.dispatcher
            )
        )
    }

    @Nested
    @DisplayName("Given a character that is a favorite")
    inner class FavoriteCharacter {

        private val favorite = ModelStubs.characterOne.copy(isFavorite = true)

        @Test
        fun `When user remove it from favorite Then it should emit character removed`() =
            coroutinesTestExtension.runBlockingTest {

                val notFavorite = favorite.copy(isFavorite = false)

                every { removeFromFavorite.execute(favorite) } returns success(notFavorite)

                val eventsObserver = favoritesViewModel.favoriteStatusChanged.testObserver()

                val expectedEvents = listOf(FavoritesViewModel.FavoriteStatusChanged(notFavorite))

                favoritesViewModel.removeFromFavorite(favorite)

                assertArrayEquals(
                    expectedEvents.toTypedArray(),
                    eventsObserver.observedValues.toTypedArray()
                )
            }
    }

    @Nested
    @DisplayName("Given a character that is not a favorite")
    inner class NotFavoriteCharacter {

        private val notFavorite = ModelStubs.characterOne.copy(isFavorite = false)

        @Test
        fun `When user add it to favorite Then it should emit character added`() =
            coroutinesTestExtension.runBlockingTest {

                val favorite = notFavorite.copy(isFavorite = true)

                every { addToFavorite.execute(notFavorite) } returns success(favorite)
                every { fetchAndSaveComicsAndSeries.execute(any()) } returns success(true)

                val eventsObserver = favoritesViewModel.favoriteStatusChanged.testObserver()

                val expectedEvents = listOf(FavoritesViewModel.FavoriteStatusChanged(favorite))

                favoritesViewModel.addToFavorite(notFavorite)

                assertEquals(expectedEvents, eventsObserver.observedValues)
            }
    }
}