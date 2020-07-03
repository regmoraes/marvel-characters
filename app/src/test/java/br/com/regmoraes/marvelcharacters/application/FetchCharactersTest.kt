package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.CharacterStubs.characterOne
import br.com.regmoraes.marvelcharacters.CharacterStubs.characterTwo
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_LIMIT
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_OFFSET
import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.assertFailsWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FetchCharactersTest {

    @Nested
    @DisplayName("Given a FetchCharacters request")
    inner class FetchCharactersRequest {

        private lateinit var fetchCharacters: FetchCharacters
        private lateinit var characterRepository: CharacterRepository

        @BeforeEach
        fun setUp() {
            characterRepository = mockk()
            fetchCharacters = FetchCharacters(characterRepository)
        }

        @Test
        fun `When request is successful Then it should return character fetched`() = runBlocking {

            val characters = listOf(characterOne, characterTwo)
            coEvery {
                characterRepository.getCharacters(
                    any(),
                    any()
                )
            } returns CharacterEvent.CharactersFetched(characters)

            val event = fetchCharacters.execute()

            assertTrue(
                event is CharacterEvent.CharactersFetched && event.characters == characters
            )
        }

        @Test
        fun `When request has error Then it should return fetch error`() = runBlocking {

            coEvery {
                characterRepository.getCharacters(
                    any(),
                    any()
                )
            } returns CharacterEvent.FetchError(IllegalStateException("An error"))

            val event = fetchCharacters.execute()

            assertTrue(event is CharacterEvent.FetchError)
        }

        @Test
        fun `When limit is greater than 20 Then it should throw an error`() = runBlocking {

            val exception =
                assertFailsWith<IllegalArgumentException> { fetchCharacters.execute(limit = 1) }

            assert(exception.message == "Limit must be equal to $DEFAULT_LIMIT")
        }

        @Test
        fun `When limit is smaller than 20 Then it should throw an error`() = runBlocking {

            val exception =
                assertFailsWith<IllegalArgumentException> { fetchCharacters.execute(limit = 21) }

            assert(exception.message == "Limit must be equal to $DEFAULT_LIMIT")
        }

        @Test
        fun `When offset is smaller than 0 Then it should throw an error`() = runBlocking {

            val exception =
                assertFailsWith<IllegalArgumentException> { fetchCharacters.execute(offset = -1) }

            assert(exception.message == "Offset must be equal or greater than $DEFAULT_OFFSET")
        }
    }
}