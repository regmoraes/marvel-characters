package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.model.Series

interface SeriesRepository {

    fun insertSeries(characterId: Long, series: List<Series>): Event<Boolean>

    fun getSeries(characterId: Long): Event<List<Series>>

    fun removeSeries(characterId: Long): Event<Boolean>
}