package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository

class FetchCharacters(private val characterRepository: CharacterRepository) {

    companion object {
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 20
    }

    suspend fun execute(
        offset: Int = DEFAULT_OFFSET,
        limit: Int = DEFAULT_LIMIT
    ): CharacterEvent = characterRepository.getCharacters(offset, limit)
}