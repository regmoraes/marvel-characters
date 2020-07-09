package br.com.regmoraes.marvelcharacters.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.regmoraes.marvelcharacters.application.*
import br.com.regmoraes.marvelcharacters.application.Event.Companion.handle
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.Comic
import br.com.regmoraes.marvelcharacters.model.Series
import br.com.regmoraes.marvelcharacters.presentation.CoroutineContextProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val addToFavorite: AddToFavorite,
    private val removeFromFavorite: RemoveFromFavorite,
    private val fetchFavorites: FetchFavorites,
    private val fetchAndSaveComicsAndSeries: FetchAndSaveComicsAndSeries,
    private val saveComics: SaveComics,
    private val saveSeries: SaveSeries,
    private val dispatcher: CoroutineContextProvider = CoroutineContextProvider()
) : ViewModel() {

    private val _favoriteStatusChanged = MutableLiveData<FavoriteStatusChanged>()
    val favoriteStatusChanged: LiveData<FavoriteStatusChanged> = _favoriteStatusChanged

    private val _favoritesState = MutableLiveData<FavoritesState>()
    val favoritesState: LiveData<FavoritesState> = _favoritesState

    fun fetchFavorites() = viewModelScope.launch(dispatcher.main) {
        fetchFavorites.execute().collect { event ->
            event.handle(
                onSuccess = { _favoritesState.postValue(FavoritesState(characters = it)) },
                onError = { }
            )
        }
    }

    fun addToFavorite(
        character: Character,
        comics: List<Comic>? = null,
        series: List<Series>? = null
    ) = viewModelScope.launch(dispatcher.main) {
        withContext(dispatcher.io) { addToFavorite.execute(character) }.handle(
            onSuccess = { _favoriteStatusChanged.value = FavoriteStatusChanged(it) },
            onError = { })

        launch(dispatcher.main) {
            if (comics == null || series == null) {
                fetchAndSaveComicsAndSeries.execute(character)
            } else {
                saveComics.execute(character, comics)
                saveSeries.execute(character, series)
            }
        }
    }

    fun removeFromFavorite(character: Character) = viewModelScope.launch(dispatcher.main) {
        withContext(dispatcher.io) { removeFromFavorite.execute(character) }.handle(
            onSuccess = { _favoriteStatusChanged.value = FavoriteStatusChanged(it) },
            onError = { }
        )
    }

    data class FavoritesState(val characters: List<Character> = emptyList())

    data class FavoriteStatusChanged(val character: Character)
}
