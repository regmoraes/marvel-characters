package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository

class FetchCharacters(private val characterRepository: CharacterRepository) {

    private val defaultOffset = 0
    private val defaultLimit = 20

    suspend fun execute(
        offset: Int = defaultOffset,
        limit: Int = defaultLimit
    ): CharacterEvent = characterRepository.getCharacters(offset, limit)
}