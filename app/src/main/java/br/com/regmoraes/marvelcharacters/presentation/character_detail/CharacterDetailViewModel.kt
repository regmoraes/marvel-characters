package br.com.regmoraes.marvelcharacters.presentation.character_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.regmoraes.marvelcharacters.application.*
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterDetailViewModel(
    private val fetchComics: FetchComics,
    private val fetchSeries: FetchSeries,
    private val changeFavoriteStatus: ChangeFavoriteStatus
) : ViewModel() {

    private val _favoritesEvents = MutableLiveData<FavoritesEvent>()
    val favoritesEvents: LiveData<FavoritesEvent> = _favoritesEvents

    private val _characterEvents = MutableLiveData<CharacterEvent>()
    val characterEvents: LiveData<CharacterEvent> = _characterEvents

    fun fetchComics(characterId: Long) = viewModelScope.launch {
        _characterEvents.value = CharacterEvent.FetchingComics
        _characterEvents.postValue(fetchComics.execute(characterId))
    }

    fun fetchSeries(characterId: Long) = viewModelScope.launch {
        _characterEvents.value = CharacterEvent.FetchingSeries
        _characterEvents.postValue(fetchSeries.execute(characterId))
    }

    fun changeFavoriteStatus(character: Character) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _favoritesEvents.postValue(changeFavoriteStatus.execute(character))
        }
    }
}
