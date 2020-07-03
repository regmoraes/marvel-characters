package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.model.Character

sealed class FavoritesEvent {
    data class FavoriteAdded(val addedCharacter: Character) : FavoritesEvent()
    data class FavoriteRemoved(val removedCharacter: Character) : FavoritesEvent()
    data class FavoriteError(val error: Throwable) : FavoritesEvent()
    data class FavoritesFetched(val favorites: List<Character>) : FavoritesEvent()
}