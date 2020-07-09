package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.ComicRepository

class FetchComics(private val comicRepository: ComicRepository) {

    fun execute(characterId: Long) = comicRepository.getComics(characterId)
}