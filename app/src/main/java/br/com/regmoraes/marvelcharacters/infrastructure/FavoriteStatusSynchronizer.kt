package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.model.Character

class FavoriteStatusSynchronizer {

    private var favoritesMap = mapOf<Long, Character>()

    fun setSourceFavorites(list: List<Character>) {
        favoritesMap = list.associateBy { it.id }
    }

    fun syncFavoriteStatus(characters: List<Character>): List<Character> {
        return characters.map { it.copy(isFavorite = favoritesMap.containsKey(it.id)) }
    }
}