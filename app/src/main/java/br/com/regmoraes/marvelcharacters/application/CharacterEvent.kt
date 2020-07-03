package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.Comic
import br.com.regmoraes.marvelcharacters.model.Series

sealed class CharacterEvent {
    data class CharactersFetched(val characters: List<Character>) : CharacterEvent()
    object FetchingCharacters : CharacterEvent()

    data class SeriesFetched(val series: List<Series>) : CharacterEvent()
    object FetchingSeries : CharacterEvent()

    data class ComicsFetched(val comics: List<Comic>) : CharacterEvent()
    object FetchingComics : CharacterEvent()

    data class FetchError(val error: Throwable) : CharacterEvent()
}