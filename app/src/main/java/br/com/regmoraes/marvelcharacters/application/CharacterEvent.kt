package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.Comic
import br.com.regmoraes.marvelcharacters.model.Series

sealed class CharacterEvent {
    data class CharactersFetched(val characters: List<Character>) : CharacterEvent()
    data class SeriesFetched(val series: List<Series>) : CharacterEvent()
    data class ComicsFetched(val comics: List<Comic>) : CharacterEvent()
    data class FetchError(val error: Throwable) : CharacterEvent()
}