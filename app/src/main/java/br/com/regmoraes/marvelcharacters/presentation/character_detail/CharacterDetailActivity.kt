package br.com.regmoraes.marvelcharacters.presentation.character_detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_character_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var character: CharacterParcel
    private val viewModel by viewModel<CharacterDetailViewModel>()
    private val comicAdapter = ComicsAdapter()
    private val seriesAdapter = SeriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_detail)
        setSupportActionBar(toolbar)

        if (intent.hasExtra(CharacterParcel::class.java.simpleName)) {
            character = intent.getParcelableExtra(CharacterParcel::class.java.simpleName)!!
        }

        comicsList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        comicsList.adapter = comicAdapter

        seriesList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        seriesList.adapter = seriesAdapter

        showCharacterDetails()

        observeEvents()
        viewModel.fetchComics(character.id)
        viewModel.fetchSeries(character.id)
    }

    private fun observeEvents() {

        viewModel.characterEvents.observe(this, Observer { event ->
            when (event) {
                is CharacterEvent.ComicsFetched -> comicAdapter.submitData(event.comics)
            }
        })

        viewModel.characterEvents.observe(this, Observer { event ->
            when (event) {
                is CharacterEvent.SeriesFetched -> seriesAdapter.submitData(event.series)
            }
        })

        viewModel.favoritesEvents.observe(this, Observer { event ->
            when (event) {
                is FavoritesEvent.FavoriteAdded -> character = event.addedCharacter.toParcel()
                is FavoritesEvent.FavoriteRemoved -> character = event.removedCharacter.toParcel()
            }
            invalidateOptionsMenu()
        })
    }

    private fun showCharacterDetails() {
        supportActionBar?.title = character.name

        Picasso.get().load(character.thumbnail.pathWithExtension)
            .into(characterImageView)

        descriptionTextView.text = character.description
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_character_detail, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (character.isFavorite) {
            menu?.findItem(R.id.action_favorite)?.setIcon(R.drawable.ic_baseline_star_24)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            viewModel.changeFavoriteStatus(character.toDomain())
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}