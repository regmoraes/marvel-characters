package br.com.regmoraes.marvelcharacters.presentation.character_detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.presentation.favorites.FavoritesViewModel
import br.com.regmoraes.marvelcharacters.presentation.model.CharacterParcel
import br.com.regmoraes.marvelcharacters.presentation.model.toDomain
import br.com.regmoraes.marvelcharacters.presentation.model.toParcel
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_character_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharacterDetailsFragment : Fragment() {

    private lateinit var character: CharacterParcel
    private var menu: Menu? = null
    private val favoritesViewModel by sharedViewModel<FavoritesViewModel>()
    private val seriesViewModel by viewModel<SeriesViewModel>()
    private val comicsViewModel by viewModel<ComicsViewModel>()
    private val comicAdapter = ComicsAdapter()
    private val seriesAdapter = SeriesAdapter()

    private var scrollRange = -1
    private var favoriteActionIsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_character_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        character = CharacterDetailsFragmentArgs.fromBundle(requireArguments()).character

        comicsList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        comicsList.adapter = comicAdapter

        seriesList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        seriesList.adapter = seriesAdapter

        appBarLayout.addOnOffsetChangedListener(FavoriteToolbarAction())

        favorite_fab.setOnClickListener {
            if (character.isFavorite)
                favoritesViewModel.removeFromFavorite(character.toDomain())
            else
                favoritesViewModel.addToFavorite(
                    character.toDomain(),
                    comicAdapter.comics,
                    seriesAdapter.series
                )
        }

        showCharacterDetails()

        observeEvents()
        comicsViewModel.fetchComics(character.id)
        seriesViewModel.fetchSeries(character.id)
    }

    private fun observeEvents() {

        favoritesViewModel.favoriteStatusChanged.observe(viewLifecycleOwner, Observer { state ->
            character = state.character.toParcel()
            showFavoriteStatus()
        })

        comicsViewModel.comicsState.observe(viewLifecycleOwner, Observer { state ->

            progressBar.visibility = if (state.fetching) View.VISIBLE else View.GONE

            if (state.comics?.isNotEmpty() == true) {
                comicAdapter.submitData(state.comics)
                comicsHeader.visibility = View.VISIBLE
                comicsList.visibility = View.VISIBLE
            } else {
                comicsHeader.visibility = View.GONE
                comicsList.visibility = View.GONE
            }
        })

        seriesViewModel.seriesState.observe(viewLifecycleOwner, Observer { state ->

            progressBar.visibility = if (state.fetching) View.VISIBLE else View.GONE

            if (state.series?.isNotEmpty() == true) {
                seriesAdapter.submitData(state.series)
                seriesHeader.visibility = View.VISIBLE
                seriesList.visibility = View.VISIBLE
            } else {
                seriesHeader.visibility = View.GONE
                seriesList.visibility = View.GONE
            }
        })
    }

    private fun showCharacterDetails() {
        toolbar.title = character.name

        Picasso.get().load(character.thumbnail.pathWithExtension)
            .into(characterImageView)

        character.description.apply {
            if (isBlank())
                descriptionTextView.visibility = View.GONE
            else
                descriptionTextView.text = this
        }

        showFavoriteStatus()
    }

    private fun showFavoriteStatus() {
        val favoriteIcon = if (character.isFavorite)
            R.drawable.ic_baseline_star_24
        else
            R.drawable.ic_baseline_star_border_24

        favorite_fab.setImageDrawable(context?.getDrawable(favoriteIcon))
        menu?.findItem(R.id.action_favorite)?.setIcon(favoriteIcon)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_character_detail, menu)
        this.menu = menu
        hideFavoriteAction()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        showFavoriteStatus()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_favorite) {
            if (character.isFavorite)
                favoritesViewModel.removeFromFavorite(character.toDomain())
            else
                favoritesViewModel.addToFavorite(
                    character.toDomain(),
                    comicAdapter.comics,
                    seriesAdapter.series
                )
            return true
        } else
            false
    }

    private fun hideFavoriteAction() {
        favoriteActionIsShown = false
        menu?.findItem(R.id.action_favorite)?.isVisible = favoriteActionIsShown
    }

    private fun showFavoriteAction() {
        favoriteActionIsShown = true
        menu?.findItem(R.id.action_favorite)?.isVisible = favoriteActionIsShown
    }

    private inner class FavoriteToolbarAction : AppBarLayout.OnOffsetChangedListener {
        override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }
            if (scrollRange + verticalOffset == 0) {
                showFavoriteAction()
            } else if (favoriteActionIsShown) {
                hideFavoriteAction()
            }
        }
    }
}