package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getFavorites(): Flow<Event<List<Character>>>

    fun insertFavorite(character: Character): Event<Boolean>

    fun removeFavorite(character: Character): Event<Boolean>
}