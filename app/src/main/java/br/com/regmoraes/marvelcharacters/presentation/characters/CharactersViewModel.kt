package br.com.regmoraes.marvelcharacters.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.regmoraes.marvelcharacters.application.Event.Companion.handle
import br.com.regmoraes.marvelcharacters.application.FetchCharacters
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.presentation.CoroutineContextProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharactersViewModel(
    private val fetchCharacters: FetchCharacters,
    private val dispatcher: CoroutineContextProvider = CoroutineContextProvider()
) : ViewModel() {

    private val _charactersState = MutableLiveData<CharactersState>()
    val charactersState: LiveData<CharactersState> = _charactersState

    init {
        fetchCharacters.execute()
    }

    fun fetchCharacters(offset: Int, limit: Int) = viewModelScope.launch(dispatcher.main) {
        _charactersState.value = CharactersState(fetching = true)

        withContext(dispatcher.io) { fetchCharacters.execute(offset, limit) }.handle(
            onSuccess = { _charactersState.value = CharactersState(characters = it) },
            onError = { _charactersState.value = CharactersState(networkError = true) }
        )
    }

    data class CharactersState(
        val fetching: Boolean = false,
        val characters: List<Character>? = null,
        val networkError: Boolean = false
    )
}
