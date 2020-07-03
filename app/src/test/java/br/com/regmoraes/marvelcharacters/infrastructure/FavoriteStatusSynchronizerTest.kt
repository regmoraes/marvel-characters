package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterOne
import br.com.regmoraes.marvelcharacters.model.ModelStubs.characterTwo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FavoriteStatusSynchronizerTest {

    @Nested
    @DisplayName("Given an empty synchronizer")
    inner class EmptySynchronizer {

        val favoritesSynchronizer = FavoriteStatusSynchronizer()

        @Test
        fun `When syncing a list of characters Then it should return the same list`() {

            val unsyncedFavorites = listOf(characterOne, characterTwo)

            assertEquals(
                unsyncedFavorites,
                favoritesSynchronizer.syncFavoriteStatus(unsyncedFavorites)
            )
        }
    }

    @Nested
    @DisplayName("Given a non empty synchronizer")
    inner class NonEmptySynchronizer {

        val favoritesSynchronizer = FavoriteStatusSynchronizer()

        @Test
        fun `When syncing a list of characters Then it should return a synced list`() {

            val favorite = characterTwo.copy(isFavorite = true)
            favoritesSynchronizer.setSourceFavorites(listOf(favorite))

            val unsyncedFavorites = listOf(characterOne, characterTwo)

            val expectedSyncedFavorites = listOf(characterOne, favorite)

            assertEquals(
                expectedSyncedFavorites,
                favoritesSynchronizer.syncFavoriteStatus(unsyncedFavorites)
            )
        }
    }
}