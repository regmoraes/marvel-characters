package br.com.regmoraes.marvelcharacters.presentation.character

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.application.FetchCharacters
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_LIMIT
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_OFFSET
import br.com.regmoraes.marvelcharacters.extension.CoroutinesTestExtension
import br.com.regmoraes.marvelcharacters.extension.InstantExecutorExtension
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterTwo
import br.com.regmoraes.marvelcharacters.presentation.CoroutineContextProvider
import br.com.regmoraes.marvelcharacters.presentation.characters.CharactersViewModel
import br.com.regmoraes.marvelcharacters.presentation.testObserver
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(InstantExecutorExtension::class)
class CharactersViewModelTest {

    private lateinit var fetchCharacters: FetchCharacters
    private lateinit var charactersViewModel: CharactersViewModel

    @JvmField
    @RegisterExtension
    val coroutinesTestExtension = CoroutinesTestExtension()

    @BeforeEach
    fun setUp() {
        fetchCharacters = mockk()
    }

    @Nested
    @DisplayName("Given a fetch characters request")
    inner class FetchCharactersRequest {

        @Test
        fun `When it is made Then it should emit fetching and fetched character if successful`() =
            coroutinesTestExtension.runBlockingTest {

                val characters = listOf(characterOne, characterTwo)
                every {
                    fetchCharacters.execute(
                        DEFAULT_OFFSET,
                        DEFAULT_LIMIT
                    )
                } returns Event.success(characters)

                charactersViewModel = CharactersViewModel(
                    fetchCharacters,
                    CoroutineContextProvider(
                        coroutinesTestExtension.dispatcher,
                        coroutinesTestExtension.dispatcher
                    )
                )

                val eventsObserver = charactersViewModel.charactersState.testObserver()

                val expectedEvents =
                    listOf(CharactersViewModel.CharactersState(characters = characters))

                assertEquals(expectedEvents, eventsObserver.observedValues)
            }

        @Test
        fun `When it is made Then it should emit fetching and fetched error if it has error`() =
            coroutinesTestExtension.runBlockingTest {

                every { fetchCharacters.execute() } returns Event.error(IllegalStateException("Some error"))

                charactersViewModel = CharactersViewModel(
                    fetchCharacters,
                    CoroutineContextProvider(
                        coroutinesTestExtension.dispatcher,
                        coroutinesTestExtension.dispatcher
                    )
                )

                val eventsObserver = charactersViewModel.charactersState.testObserver()

                val expectedEvents =
                    listOf(CharactersViewModel.CharactersState(networkError = true))

                assertEquals(expectedEvents, eventsObserver.observedValues)
            }
    }
}