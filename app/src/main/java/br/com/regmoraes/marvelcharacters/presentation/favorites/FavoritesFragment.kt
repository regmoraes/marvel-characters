package br.com.regmoraes.marvelcharacters.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.application.FavoritesEvent
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.presentation.home.HomeViewModel
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FavoritesFragment : Fragment() {

    private val viewModel by sharedViewModel<HomeViewModel>()
    private var favoriteAdapter =
        FavoriteAdapter(object : FavoriteAdapter.OnClickListener {
            override fun onFavoriteClicked(character: Character) {
                viewModel.changeFavoriteStatus(character)
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.favoritesEvents.observe(viewLifecycleOwner, Observer { event ->
            when (event) {
                is FavoritesEvent.FavoritesFetched -> favoriteAdapter.submitData(event.favorites)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesList.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = favoriteAdapter
        }
    }
}