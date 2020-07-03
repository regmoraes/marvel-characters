package br.com.regmoraes.marvelcharacters.infrastructure.api

import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.Comic
import br.com.regmoraes.marvelcharacters.model.Series
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterRestService {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): DataWrapper<Character>

    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getComics(
        @Path("characterId") characterId: Long
    ): DataWrapper<Comic>

    @GET("/v1/public/characters/{characterId}/series")
    suspend fun getSeries(
        @Path("characterId") characterId: Long
    ): DataWrapper<Series>
}