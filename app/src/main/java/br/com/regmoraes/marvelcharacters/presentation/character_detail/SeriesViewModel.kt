package br.com.regmoraes.marvelcharacters.presentation.character_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.regmoraes.marvelcharacters.application.Event.Companion.handle
import br.com.regmoraes.marvelcharacters.application.FetchSeries
import br.com.regmoraes.marvelcharacters.model.Series
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SeriesViewModel(private val fetchSeries: FetchSeries) : ViewModel() {

    private val _seriesState = MutableLiveData<SeriesState>()
    val seriesState: LiveData<SeriesState> = _seriesState

    fun fetchSeries(characterId: Long) = viewModelScope.launch {
        _seriesState.value = SeriesState(fetching = true)
        withContext(Dispatchers.IO) {
            fetchSeries.execute(characterId).handle(
                onSuccess = { _seriesState.postValue(SeriesState(series = it)) },
                onError = { _seriesState.postValue(SeriesState(networkError = true)) }
            )
        }
    }

    data class SeriesState(
        val fetching: Boolean = false,
        val series: List<Series>? = null,
        val networkError: Boolean = false
    )
}
