package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.CharacterEvent
import br.com.regmoraes.marvelcharacters.application.FavoritesEvent
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.database.CharacterDao
import br.com.regmoraes.marvelcharacters.infrastructure.database.toCharacter
import br.com.regmoraes.marvelcharacters.infrastructure.database.toEntity
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class CharacterRepositoryMediator(
    private val characterDao: CharacterDao,
    private val characterRestService: CharacterRestService,
    private val favoriteStatusSynchronizer: FavoriteStatusSynchronizer
) : CharacterRepository {

    override suspend fun getCharacters(
        offset: Int,
        limit: Int
    ): CharacterEvent {

        return try {

            val response = characterRestService.getCharacters(
                offset = offset,
                limit = limit
            )
            val characters =
                favoriteStatusSynchronizer.syncFavoriteStatus(response.data.results)
            if (response.code == 200) {
                CharacterEvent.CharactersFetched(characters)
            } else {
                CharacterEvent.FetchError(IllegalStateException(response.status))
            }
        } catch (e: Exception) {
            Timber.d(e)
            CharacterEvent.FetchError(e)
        }
    }

    override suspend fun getComics(characterId: Long): CharacterEvent {
        return try {
            val response = characterRestService.getComics(characterId)

            if (response.code == 200) {
                CharacterEvent.ComicsFetched(response.data.results)
            } else {
                CharacterEvent.FetchError(IllegalStateException(response.status))
            }
        } catch (e: Exception) {
            Timber.d(e)
            CharacterEvent.FetchError(e)
        }
    }

    override suspend fun getSeries(characterId: Long): CharacterEvent {
        return try {
            val response = characterRestService.getSeries(characterId)

            if (response.code == 200) {
                CharacterEvent.SeriesFetched(response.data.results)
            } else {
                CharacterEvent.FetchError(IllegalStateException(response.status))
            }
        } catch (e: Exception) {
            Timber.d(e)
            CharacterEvent.FetchError(e)
        }
    }

    override suspend fun insertFavorite(character: Character): FavoritesEvent {
        val addedEntityId = characterDao.insertAsFavorite(character.toEntity())
        return if (addedEntityId != -1L) {
            FavoritesEvent.FavoriteAdded(character)
        } else {
            FavoritesEvent.FavoriteError(IllegalStateException("Cannot add"))
        }
    }

    override suspend fun getFavorites(): Flow<FavoritesEvent.FavoritesFetched> {
        return characterDao.getAllFavorites()
            .map { charactersEntity -> FavoritesEvent.FavoritesFetched(charactersEntity.map { it.toCharacter() }) }
            .onEach { favoriteStatusSynchronizer.setSourceFavorites(it.favorites) }
    }

    override suspend fun removeFavorite(character: Character): FavoritesEvent {
        val removedEntityId = characterDao.removeFavorite(character.id)
        return if (removedEntityId > 0) {
            FavoritesEvent.FavoriteRemoved(character)
        } else {
            FavoritesEvent.FavoriteError(IllegalStateException("Cannot remove"))
        }
    }
}
