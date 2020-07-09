package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.coroutines.flow.Flow

class FetchFavorites(private val characterRepository: CharacterRepository) {

    fun execute(): Flow<Event<List<Character>>> = characterRepository.getFavorites()
}