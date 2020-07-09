package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.application.Event.Companion.flatMap
import br.com.regmoraes.marvelcharacters.model.Character

class FetchAndSaveComicsAndSeries(
    private val fetchComics: FetchComics,
    private val saveComics: SaveComics,
    private val fetchSeries: FetchSeries,
    private val saveSeries: SaveSeries
) {

    fun execute(character: Character): Event<Boolean> {
        return fetchComics.execute(character.id)
            .flatMap { comics -> saveComics.execute(character, comics) }
            .flatMap { fetchSeries.execute(character.id) }
            .flatMap { series -> saveSeries.execute(character, series) }
    }
}
