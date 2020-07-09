package br.com.regmoraes.marvelcharacters.presentation.characters

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_LIMIT
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_OFFSET
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.presentation.favorites.FavoritesViewModel
import br.com.regmoraes.marvelcharacters.presentation.home.HomeFragmentDirections
import br.com.regmoraes.marvelcharacters.presentation.model.toParcel
import kotlinx.android.synthetic.main.adapter_character.*
import kotlinx.android.synthetic.main.fragment_characters.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CharactersFragment : Fragment(), CharacterAdapter.OnClickListener {

    private val favoritesViewModel by sharedViewModel<FavoritesViewModel>()
    private val charactersViewModel by viewModel<CharactersViewModel>()

    private var charactersAdapter = CharacterAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        charactersViewModel.charactersState.observe(
            parentFragment?.viewLifecycleOwner ?: viewLifecycleOwner, Observer { state ->

                swipeRefresh.isRefreshing = state.fetching

                if (!state.characters.isNullOrEmpty()) {
                    charactersAdapter.submitData(state.characters)
                }

                if (state.networkError) {
                    charactersStatus.visibility = View.VISIBLE
                    charactersStatus.text = getString(R.string.generic_network_error)
                    charactersList.visibility = View.INVISIBLE
                } else {
                    charactersStatus.visibility = View.GONE
                    charactersList.visibility = View.VISIBLE
                }
            })

        favoritesViewModel.favoriteStatusChanged.observe(
            parentFragment?.viewLifecycleOwner ?: viewLifecycleOwner, Observer { state ->
                charactersAdapter.updateFavoriteStatus(state.character)
            })

        swipeRefresh.setOnRefreshListener {
            charactersViewModel.fetchCharacters(DEFAULT_OFFSET, DEFAULT_LIMIT)
        }

        charactersList.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = charactersAdapter
            addOnScrollListener(object :
                RecyclerViewPagination(layoutManager as StaggeredGridLayoutManager) {
                override fun loadMore(offset: Int, limit: Int) {
                    charactersViewModel.fetchCharacters(offset, limit)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_character_search, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                charactersAdapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun onFavoriteClicked(character: Character) {
        if (character.isFavorite)
            favoritesViewModel.removeFromFavorite(character)
        else
            favoritesViewModel.addToFavorite(character)
    }

    override fun onCharacterClicked(character: Character) {
        val extras =
            FragmentNavigatorExtras(characterImageView to getString(R.string.character_image_transition))
        val action = HomeFragmentDirections.actionHomeToCharacterDetails(character.toParcel())
        findNavController().navigate(action, extras)
    }
}