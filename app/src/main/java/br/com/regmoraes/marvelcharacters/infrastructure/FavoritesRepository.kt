package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.FavoritesEvent
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun getFavorites(): Flow<FavoritesEvent.FavoritesFetched>

    suspend fun insertFavorite(character: Character): FavoritesEvent

    suspend fun removeFavorite(character: Character): FavoritesEvent
}