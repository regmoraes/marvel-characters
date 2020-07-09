package br.com.regmoraes.marvelcharacters.infrastructure

import br.com.regmoraes.marvelcharacters.application.Event
import br.com.regmoraes.marvelcharacters.application.Event.Companion.error
import br.com.regmoraes.marvelcharacters.application.Event.Companion.success
import br.com.regmoraes.marvelcharacters.infrastructure.api.CharacterRestService
import br.com.regmoraes.marvelcharacters.infrastructure.database.ComicsDao
import br.com.regmoraes.marvelcharacters.infrastructure.database.toComic
import br.com.regmoraes.marvelcharacters.infrastructure.database.toEntity
import br.com.regmoraes.marvelcharacters.model.Comic

class ComicRepositoryMediator(
    private val comicsDao: ComicsDao,
    private val characterRestService: CharacterRestService
) : ComicRepository {

    override fun insertComics(characterId: Long, comics: List<Comic>): Event<Boolean> {
        val insertedComics = comicsDao.insertComics(comics.map { it.toEntity(characterId) })
        return if (insertedComics.size == comics.size) {
            success(true)
        } else {
            error(java.lang.IllegalStateException("Cannot insert comics"))
        }
    }

    override fun getComics(characterId: Long): Event<List<Comic>> {

        val localComics = comicsDao.getComics(characterId)

        return if (localComics.isEmpty()) {
            try {
                val response = characterRestService.getComics(characterId).execute()
                if (response.isSuccessful) {
                    val resultData = response.body()?.data?.results ?: emptyList()
                    success(resultData)
                } else {
                    error(IllegalStateException(response.message()))
                }
            } catch (e: Exception) {
                error(e)
            }
        } else {
            success(localComics.map { it.toComic() })
        }
    }

    override fun removeComics(characterId: Long): Event<Boolean> {
        return success(comicsDao.removeComics(characterId) > 0)
    }
}
