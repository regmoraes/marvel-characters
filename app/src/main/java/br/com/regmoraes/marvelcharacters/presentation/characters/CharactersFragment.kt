package br.com.regmoraes.marvelcharacters.presentation.characters

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.application.CharacterEvent
import br.com.regmoraes.marvelcharacters.application.FavoritesEvent
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_LIMIT
import br.com.regmoraes.marvelcharacters.application.FetchCharacters.Companion.DEFAULT_OFFSET
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.presentation.character_detail.CharacterDetailActivity
import br.com.regmoraes.marvelcharacters.presentation.home.HomeViewModel
import br.com.regmoraes.marvelcharacters.presentation.model.CharacterParcel
import br.com.regmoraes.marvelcharacters.presentation.model.toParcel
import kotlinx.android.synthetic.main.fragment_characters.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CharactersFragment : Fragment(), CharacterAdapter.OnClickListener {

    private val viewModel by sharedViewModel<HomeViewModel>()
    private var charactersAdapter = CharacterAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.characterEvents.observe(viewLifecycleOwner, Observer { event ->
            when (event) {
                is CharacterEvent.FetchingCharacters -> {
                    swipeRefresh.isRefreshing = true
                    charactersStatus.visibility = View.GONE
                }
                is CharacterEvent.CharactersFetched -> {
                    charactersAdapter.submitData(event.characters)
                    swipeRefresh.isRefreshing = false
                    charactersStatus.visibility = View.GONE
                }
                is CharacterEvent.FetchError -> {
                    charactersStatus.visibility = View.VISIBLE
                    charactersStatus.text = event.error.message
                    swipeRefresh.isRefreshing = false
                }
            }
        })

        viewModel.favoritesEvents.observe(viewLifecycleOwner, Observer { event ->
            when (event) {
                is FavoritesEvent.FavoriteAdded -> {
                    charactersAdapter.updateFavoriteStatus(event.addedCharacter)
                }
                is FavoritesEvent.FavoriteRemoved -> {
                    charactersAdapter.updateFavoriteStatus(event.removedCharacter)
                }
            }
        })

        viewModel.fetchCharacters(DEFAULT_OFFSET, DEFAULT_LIMIT)
        viewModel.fetchFavorites()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        charactersList.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            adapter = charactersAdapter
            addOnScrollListener(object :
                RecyclerViewPagination(layoutManager as StaggeredGridLayoutManager) {
                override fun loadMore(offset: Int, limit: Int) {
                    viewModel.fetchCharacters(offset, limit)
                }
            })
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.fetchCharacters(DEFAULT_OFFSET, DEFAULT_LIMIT)
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
        viewModel.changeFavoriteStatus(character)
    }

    override fun onCharacterClicked(character: Character) {
        val intent = Intent(
            activity,
            CharacterDetailActivity::class.java
        ).putExtra(CharacterParcel::class.simpleName, character.toParcel())

        startActivity(intent)
    }
}