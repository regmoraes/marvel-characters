package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.changeFavoriteStatus


class ChangeFavoriteStatus(private val characterRepository: CharacterRepository) {

    fun execute(character: Character): FavoritesEvent {

        val changedCharacter = character.changeFavoriteStatus()

        return if (character.isFavorite) {
            characterRepository.removeFavorite(changedCharacter)
        } else {
            characterRepository.insertFavorite(changedCharacter)
        }
    }
}
