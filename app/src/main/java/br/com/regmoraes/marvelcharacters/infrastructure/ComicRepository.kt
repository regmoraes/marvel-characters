package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.model.Comic

interface ComicRepository {

    fun insertComics(characterId: Long, comics: List<Comic>): Event<Boolean>

    fun getComics(characterId: Long): Event<List<Comic>>

    fun removeComics(characterId: Long): Event<Boolean>
}