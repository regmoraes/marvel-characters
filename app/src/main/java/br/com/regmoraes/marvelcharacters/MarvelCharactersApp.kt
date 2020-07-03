package br.com.regmoraes.marvelcharacters

import android.app.Application
import br.com.regmoraes.marvelcharacters.infrastructure.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree


class MarvelCharactersApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        startKoin {
            androidContext(this@MarvelCharactersApp)
            if (BuildConfig.DEBUG) androidLogger(Level.DEBUG)
            modules(
                applicationModule,
                presentationModule,
                infrastructureModule,
                networkModule,
                databaseModule
            )
        }
    }
}