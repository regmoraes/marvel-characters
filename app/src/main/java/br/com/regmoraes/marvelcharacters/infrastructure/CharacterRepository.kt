package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.CharacterEvent

interface CharacterRepository : FavoritesRepository {

    suspend fun getCharacters(offset: Int, limit: Int): CharacterEvent

    suspend fun getComics(characterId: Long): CharacterEvent

    suspend fun getSeries(characterId: Long): CharacterEvent
}