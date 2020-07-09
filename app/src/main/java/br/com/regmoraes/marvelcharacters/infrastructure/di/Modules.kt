package br.com.regmoraes.marvelcharacters.infrastructure.di

import br.com.regmoraes.marvelcharacters.application.*
import br.com.regmoraes.marvelcharacters.infrastructure.*
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.api.RetrofitConfiguration
import br.com.regmoraes.marvelcharacters.infrastructure.api.RetrofitConfiguration.BASE_URL
import br.com.regmoraes.marvelcharacters.infrastructure.database.MarvelCharactersDatabase
import br.com.regmoraes.marvelcharacters.presentation.CoroutineContextProvider
import br.com.regmoraes.marvelcharacters.presentation.character_detail.ComicsViewModel
import br.com.regmoraes.marvelcharacters.presentation.character_detail.SeriesViewModel
import br.com.regmoraes.marvelcharacters.presentation.characters.CharactersViewModel
import br.com.regmoraes.marvelcharacters.presentation.favorites.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single { FetchCharacters(get()) }
    single { FetchComics(get()) }
    single { SaveComics(get()) }
    single { FetchSeries(get()) }
    single { SaveSeries(get()) }
    single { FetchFavorites(get()) }
    single { AddToFavorite(get()) }
    single { RemoveFromFavorite(get()) }
    single { FetchAndSaveComicsAndSeries(get(), get(), get(), get()) }
}

val presentationModule = module {
    viewModel { CharactersViewModel(get()) }
    viewModel {
        FavoritesViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            CoroutineContextProvider()
        )
    }
    viewModel { SeriesViewModel(get()) }
    viewModel { ComicsViewModel(get()) }
}

val databaseModule = module {
    single { MarvelCharactersDatabase.getDatabase(get()).characterDao() }
    single { MarvelCharactersDatabase.getDatabase(get()).comicsDao() }
    single { MarvelCharactersDatabase.getDatabase(get()).seriesDao() }
}

val networkModule = module {
    val retrofit by lazy { RetrofitConfiguration.build(BASE_URL) }
    single { retrofit }
    single { retrofit.create(CharacterRestService::class.java) }
}

val infrastructureModule = module {
    single { FavoriteStatusSynchronizer() }
    single<CharacterRepository> { CharacterRepositoryMediator(get(), get(), get()) }
    single<SeriesRepository> { SeriesRepositoryMediator(get(), get()) }
    single<ComicRepository> { ComicRepositoryMediator(get(), get()) }
}