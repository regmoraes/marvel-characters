package br.com.regmoraes.marvelcharacters.presentation.home

import androidx.lifecycle.*
import br.com.regmoraes.marvelcharacters.application.*
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val changeFavoriteStatus: ChangeFavoriteStatus,
    private val fetchFavorites: FetchFavorites,
    private val fetchCharacters: FetchCharacters
) : ViewModel() {

    private val _characterEvents = MutableLiveData<CharacterEvent>()
    val characterEvents: LiveData<CharacterEvent> = _characterEvents

    private val _favoritesEvents = MediatorLiveData<FavoritesEvent>()
    val favoritesEvents: LiveData<FavoritesEvent> = _favoritesEvents

    fun fetchCharacters(offset: Int, limit: Int) = viewModelScope.launch {
        _characterEvents.value = CharacterEvent.FetchingCharacters
        _characterEvents.postValue(fetchCharacters.execute(offset, limit))
    }

    fun fetchFavorites() = viewModelScope.launch {
        _favoritesEvents.addSource(fetchFavorites.execute().asLiveData()) {
            _favoritesEvents.postValue(it)
        }
    }

    fun changeFavoriteStatus(character: Character) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _favoritesEvents.postValue(changeFavoriteStatus.execute(character))
        }
    }
}
