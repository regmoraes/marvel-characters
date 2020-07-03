package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import kotlinx.coroutines.flow.Flow

class FetchFavorites(private val characterRepository: CharacterRepository) {
    fun execute(): Flow<FavoritesEvent> {
        return characterRepository.getFavorites()
    }
}