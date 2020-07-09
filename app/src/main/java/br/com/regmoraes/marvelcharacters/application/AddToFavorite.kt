package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.application.Event.Companion.map
import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.Character

class AddToFavorite(private val characterRepository: CharacterRepository) {

    fun execute(character: Character): Event<Character> {
        require(!character.isFavorite)
        return characterRepository.insertFavorite(character)
            .map { inserted -> if (inserted) character.copy(isFavorite = true) else character }
    }
}
