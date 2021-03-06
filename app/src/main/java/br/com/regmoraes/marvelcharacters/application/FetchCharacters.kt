package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.Character


class FetchCharacters(private val characterRepository: CharacterRepository) {

    fun execute(offset: Int = DEFAULT_OFFSET, limit: Int = DEFAULT_LIMIT): Event<List<Character>> {
        require(offset >= DEFAULT_OFFSET) { "Offset must be equal or greater than $DEFAULT_OFFSET" }
        require(limit == DEFAULT_LIMIT) { "Limit must be equal to $DEFAULT_LIMIT" }
        return characterRepository.getCharacters(offset, limit)
    }

    companion object {
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 20
    }
}
