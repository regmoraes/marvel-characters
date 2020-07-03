package br.com.regmoraes.marvelcharacters.infrastructure.di

import br.com.regmoraes.marvelcharacters.application.*
import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepository
import br.com.regmoraes.marvelcharacters.infrastructure.CharacterRepositoryMediator
import br.com.regmoraes.marvelcharacters.infrastructure.FavoriteStatusSynchronizer
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.api.RetrofitConfiguration
import br.com.regmoraes.marvelcharacters.infrastructure.api.RetrofitConfiguration.BASE_URL
import br.com.regmoraes.marvelcharacters.infrastructure.database.MarvelCharactersDatabase
import br.com.regmoraes.marvelcharacters.presentation.character_detail.CharacterDetailViewModel
import br.com.regmoraes.marvelcharacters.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single { FetchCharacters(get()) }
    single { FetchComics(get()) }
    single { FetchSeries(get()) }
    single { FetchFavorites(get()) }
    single { ChangeFavoriteStatus(get()) }
}

val presentationModule = module {
    viewModel {
        HomeViewModel(
            get(),
            get(),
            get()
        )
    }
    viewModel { CharacterDetailViewModel(get(), get(), get()) }
}

val databaseModule = module {
    single { MarvelCharactersDatabase.getDatabase(get()).characterDao() }
}

val networkModule = module {
    val retrofit by lazy { RetrofitConfiguration.build(BASE_URL) }
    single { retrofit }
    single { retrofit.create(CharacterRestService::class.java) }
}

val infrastructureModule = module {
    single { FavoriteStatusSynchronizer() }
    single<CharacterRepository> { CharacterRepositoryMediator(get(), get(), get()) }
}