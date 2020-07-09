package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.database.CharacterDao
import br.com.regmoraes.marvelcharacters.infrastructure.database.toCharacter
import br.com.regmoraes.marvelcharacters.infrastructure.database.toEntity
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class CharacterRepositoryMediator(
    private val characterDao: CharacterDao,
    private val characterRestService: CharacterRestService,
    private val favoriteStatusSynchronizer: FavoriteStatusSynchronizer
) : CharacterRepository {

    override fun getCharacters(offset: Int, limit: Int): Event<List<Character>> {

        return try {

            val response = characterRestService.getCharacters(
                offset = offset,
                limit = limit
            ).execute()

            if (response.isSuccessful) {
                val dataResults = response.body()?.data?.results ?: emptyList()
                val characters = favoriteStatusSynchronizer.syncFavoriteStatus(dataResults)
                Event.Success(characters)
            } else {
                Event.Error(IllegalStateException(response.message()))
            }
        } catch (e: Exception) {
            Event.Error(e)
        }
    }

    override fun insertFavorite(character: Character): Event<Boolean> {
        val addedEntityId = characterDao.insertAsFavorite(character.toEntity())
        return if (addedEntityId != -1L) {
            Event.success(true)
        } else {
            Event.error(java.lang.IllegalStateException("Cannot add favorite"))
        }
    }

    override fun getFavorites(): Flow<Event<List<Character>>> {
        return characterDao.getAllFavorites()
            .map { charactersEntity -> Event.Success(charactersEntity.map { it.toCharacter() }) }
            .onEach { favoriteStatusSynchronizer.setSourceFavorites(it.data) }
    }

    override fun removeFavorite(character: Character): Event<Boolean> {
        val removedEntityId = characterDao.removeFavorite(character.id)
        return if (removedEntityId > 0) {
            Event.success(true)
        } else {
            Event.error(java.lang.IllegalStateException("Cannot remove favorite"))
        }
    }
}
