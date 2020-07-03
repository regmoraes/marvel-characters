package br.com.regmoraes.marvelcharacters.presentation.characters

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.application.CharacterEvent
import br.com.regmoraes.marvelcharacters.application.FavoritesEvent
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
                is CharacterEvent.CharactersFetched -> {
                    charactersAdapter.submitData(
                        event.characters,
                        swipeRefresh.isRefreshing
                    )
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

        viewModel.fetchCharacters(0, 20)
        viewModel.fetchFavorites()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        charactersList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = charactersAdapter
            addOnScrollListener(object : RecyclerViewPaginator(this) {
                override fun loadMore(offset: Int, limit: Int) {
                    viewModel.fetchCharacters(offset, limit)
                }
            })
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.fetchCharacters(0, 20)
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