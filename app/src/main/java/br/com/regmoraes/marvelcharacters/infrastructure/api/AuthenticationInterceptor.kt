package br.com.regmoraes.marvelcharacters.infrastructure.api

import br.com.regmoraes.marvelcharacters.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest
import java.util.*


internal object AuthenticationInterceptor : Interceptor {

    object Param {
        const val API_KEY = "apikey"
        const val HASH = "hash"
        const val TIME_STAMP = "ts"
    }

    private val md5Digest by lazy { MessageDigest.getInstance("MD5") }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val timestamp = Date().time

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(Param.API_KEY, BuildConfig.PUBLIC_KEY)
            .addQueryParameter(
                Param.HASH,
                generateHash(BuildConfig.PRIVATE_KEY, BuildConfig.PUBLIC_KEY, timestamp)
            )
            .addQueryParameter(Param.TIME_STAMP, "$timestamp")
            .build()

        val newRequest = original.newBuilder().url(url).build()

        return chain.proceed(newRequest)
    }

    fun generateHash(privateKey: String, publicKey: String, timestamp: Long): String {

        val plainTextHash = "$timestamp$privateKey$publicKey"

        md5Digest.update(plainTextHash.toByteArray())

        return md5Digest.digest().joinToString("") { "%02x".format(it) }
    }
}