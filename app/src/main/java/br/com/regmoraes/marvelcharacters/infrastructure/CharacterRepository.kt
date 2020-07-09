package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.model.Character

interface CharacterRepository : FavoritesRepository {

    fun getCharacters(offset: Int, limit: Int): Event<List<Character>>
}