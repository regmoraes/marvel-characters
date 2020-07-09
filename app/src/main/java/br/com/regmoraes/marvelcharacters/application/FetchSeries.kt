package br.com.regmoraes.marvelcharacters.application

import br.com.regmoraes.marvelcharacters.infrastructure.SeriesRepository

class FetchSeries(private val seriesRepository: SeriesRepository) {

    fun execute(characterId: Long) = seriesRepository.getSeries(characterId)
}