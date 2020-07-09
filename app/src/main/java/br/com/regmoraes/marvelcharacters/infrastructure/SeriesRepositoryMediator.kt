package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.application.Event.Companion.error
import br.com.regmoraes.marvelcharacters.application.Event.Companion.success
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.database.SeriesDao
import br.com.regmoraes.marvelcharacters.infrastructure.database.toEntity
import br.com.regmoraes.marvelcharacters.infrastructure.database.toSeries
import br.com.regmoraes.marvelcharacters.model.Series
import timber.log.Timber

class SeriesRepositoryMediator(
    private val seriesDao: SeriesDao,
    private val characterRestService: CharacterRestService
) : SeriesRepository {

    override fun insertSeries(characterId: Long, series: List<Series>): Event<Boolean> {
        val insertedSeries = seriesDao.insertSeries(series.map { it.toEntity(characterId) })
        return success(insertedSeries.size == series.size)
    }

    override fun getSeries(characterId: Long): Event<List<Series>> {

        val localSeries = seriesDao.getSeries(characterId)

        return if (localSeries.isEmpty()) {
            try {
                val response = characterRestService.getSeries(characterId).execute()

                if (response.isSuccessful) {
                    val resultData = response.body()?.data?.results ?: emptyList()
                    success(resultData)
                } else {
                    error(IllegalStateException(response.message()))
                }
            } catch (e: Exception) {
                Timber.d(e)
                error(e)
            }
        } else {
            success(localSeries.map { it.toSeries() })
        }
    }

    override fun removeSeries(characterId: Long): Event<Boolean> {
        return success(seriesDao.removeSeries(characterId) > 0)
    }
}
