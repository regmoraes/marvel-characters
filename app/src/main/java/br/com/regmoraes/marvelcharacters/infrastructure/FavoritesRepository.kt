package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.FavoritesEvent
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getFavorites(): Flow<FavoritesEvent.FavoritesFetched>

    fun insertFavorite(character: Character): FavoritesEvent

    fun removeFavorite(character: Character): FavoritesEvent
}