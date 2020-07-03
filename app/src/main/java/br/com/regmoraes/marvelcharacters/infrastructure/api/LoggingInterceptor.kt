package br.com.regmoraes.marvelcharacters.infrastructure.api

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber


internal object LoggingInterceptor : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Timber.tag("OkHttp").d(message)
    }
}