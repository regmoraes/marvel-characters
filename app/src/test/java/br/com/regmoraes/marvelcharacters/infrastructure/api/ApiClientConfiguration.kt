package br.com.regmoraes.marvelcharacters.infrastructure.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientConfiguration {

    private val loggingInterceptor = HttpLoggingInterceptor(LoggingInterceptor).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(AuthenticationInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    fun buildRetrofit(url: String) = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(okHttpClient)
        .build()
}