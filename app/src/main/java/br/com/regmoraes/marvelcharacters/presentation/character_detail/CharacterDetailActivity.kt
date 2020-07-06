package br.com.regmoraes.marvelcharacters.presentation.character_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.regmoraes.marvelcharacters.R
import br.com.regmoraes.marvelcharacters.application.CharacterEvent
import br.com.regmoraes.marvelcharacters.application.FavoritesEvent
import br.com.regmoraes.marvelcharacters.presentation.model.CharacterParcel
import br.com.regmoraes.marvelcharacters.presentation.model.toDomain
import br.com.regmoraes.marvelcharacters.presentation.model.toParcel
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var character: CharacterParcel
    private var menu: Menu? = null
    private val viewModel by viewModel<CharacterDetailViewModel>()
    private val comicAdapter = ComicsAdapter()
    private val seriesAdapter = SeriesAdapter()

    private var scrollRange = -1
    private var favoriteActionIsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(CharacterParcel::class.java.simpleName)) {
            character = intent.getParcelableExtra(CharacterParcel::class.java.simpleName)!!
        }

        comicsList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        comicsList.adapter = comicAdapter

        seriesList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        seriesList.adapter = seriesAdapter

        appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }
            if (scrollRange + verticalOffset == 0) {
                showFavoriteAction()
            } else if (favoriteActionIsShown) {
                hideFavoriteAction()
            }
        })

        favorite_fab.setOnClickListener {
            viewModel.changeFavoriteStatus(character.toDomain())
        }

        showCharacterDetails()

        observeEvents()
        viewModel.fetchComics(character.id)
        viewModel.fetchSeries(character.id)
    }

    private fun observeEvents() {

        viewModel.characterEvents.observe(this, Observer { event ->
            when (event) {
                is CharacterEvent.ComicsFetched -> {
                    event.comics.apply {
                        comicAdapter.submitData(this)
                        if (isEmpty()) {
                            comicsHeader.visibility = View.GONE
                        } else {
                            comicsHeader.visibility = View.VISIBLE
                        }
                        progressBar.visibility = View.GONE
                        comicsList.visibility = View.VISIBLE
                    }
                }
                is CharacterEvent.SeriesFetched -> {
                    event.series.apply {
                        seriesAdapter.submitData(this)
                        if (isEmpty()) {
                            seriesHeader.visibility = View.GONE
                        } else {
                            seriesHeader.visibility = View.VISIBLE
                        }
                        progressBar.visibility = View.GONE
                        seriesList.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.favoritesEvents.observe(this, Observer { event ->
            when (event) {
                is FavoritesEvent.FavoriteAdded -> character = event.addedCharacter.toParcel()
                is FavoritesEvent.FavoriteRemoved -> character = event.removedCharacter.toParcel()
            }

            showFavoriteStatus()
        })
    }

    private fun showCharacterDetails() {
        supportActionBar?.title = character.name

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

        favorite_fab.setImageDrawable(getDrawable(favoriteIcon))
        menu?.findItem(R.id.action_favorite)?.setIcon(favoriteIcon)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_character_detail, menu)
        this.menu = menu
        hideFavoriteAction()
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        showFavoriteStatus()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_favorite) {
            viewModel.changeFavoriteStatus(character.toDomain())
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
}