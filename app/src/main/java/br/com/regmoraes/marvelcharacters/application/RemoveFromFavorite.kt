package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.application.Event.Companion.map
import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.Character

class RemoveFromFavorite(private val characterRepository: CharacterRepository) {

    fun execute(character: Character): Event<Character> {
        require(character.isFavorite)
        return characterRepository.removeFavorite(character)
            .map { removed -> if (removed) character.copy(isFavorite = false) else character }
    }
}

