package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.ComicRepository
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.Comic

class SaveComics(private val comicRepository: ComicRepository) {

    fun execute(character: Character, comics: List<Comic>): Event<Boolean> =
        comicRepository.insertComics(character.id, comics)
}
