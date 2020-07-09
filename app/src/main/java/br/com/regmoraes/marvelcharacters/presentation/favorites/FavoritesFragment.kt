package br.com.regmoraes.marvelcharacters.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.presentation.home.HomeFragmentDirections
import br.com.regmoraes.marvelcharacters.presentation.model.toParcel
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FavoritesFragment : Fragment(), FavoriteAdapter.OnClickListener {

    private val favoritesViewModel by sharedViewModel<FavoritesViewModel>()
    private var favoriteAdapter = FavoriteAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.favoritesState.observe(viewLifecycleOwner, Observer { state ->
            val emptyFavorites = state.characters.isEmpty()
            favoritesStatus.visibility = if (emptyFavorites) View.VISIBLE else View.GONE
            favoritesList.visibility = if (emptyFavorites) View.INVISIBLE else View.VISIBLE
            favoriteAdapter.submitData(state.characters)
        })

        favoritesList.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = favoriteAdapter
        }

        favoritesViewModel.fetchFavorites()
    }

    override fun onCharacterClicked(character: Character) {
        val action = HomeFragmentDirections.actionHomeToCharacterDetails(character.toParcel())
        findNavController().navigate(action)
    }
}