package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository

class FetchComics(private val characterRepository: CharacterRepository) {

    suspend fun execute(characterId: Long): CharacterEvent =
        characterRepository.getComics(characterId)
}