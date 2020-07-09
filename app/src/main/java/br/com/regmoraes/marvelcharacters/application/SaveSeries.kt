package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.SeriesRepository
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.Series

class SaveSeries(private val seriesRepository: SeriesRepository) {

    fun execute(character: Character, series: List<Series>): Event<Boolean> =
        seriesRepository.insertSeries(character.id, series)
}
