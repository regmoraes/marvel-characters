package br.com.regmoraes.marvelcharacters.presentation.character_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.regmoraes.marvelcharacters.application.Event.Companion.handle
import br.com.regmoraes.marvelcharacters.application.FetchComics
import br.com.regmoraes.marvelcharacters.model.Comic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComicsViewModel(private val fetchComics: FetchComics) : ViewModel() {

    private val _comicsState = MutableLiveData<ComicsState>()
    val comicsState: LiveData<ComicsState> = _comicsState

    fun fetchComics(characterId: Long) = viewModelScope.launch {
        _comicsState.value = ComicsState(fetching = true)
        withContext(Dispatchers.IO) {
            fetchComics.execute(characterId).handle(
                onSuccess = { _comicsState.postValue(ComicsState(comics = it)) },
                onError = { _comicsState.postValue(ComicsState(networkError = true)) }
            )
        }
    }

    data class ComicsState(
        val fetching: Boolean = false,
        val comics: List<Comic>? = null,
        val networkError: Boolean = false
    )
}
