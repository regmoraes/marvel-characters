package br.com.regmoraes.marvelcharacters.infrastructure.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.regmoraes.marvelcharacters.model.Comic
import br.com.regmoraes.marvelcharacters.model.Thumbnail

@Entity(tableName = "comics")
data class ComicEntity(
    @PrimaryKey val id: Long,
    val characterId: Long,
    val title: String,
    val thumbnailPath: String,
    val thumbnailExtension: String
)

fun ComicEntity.toComic(): Comic {
    return Comic(
        id,
        title,
        Thumbnail(thumbnailPath, thumbnailExtension)
    )
}

fun Comic.toEntity(characterId: Long): ComicEntity {
    return ComicEntity(
        id,
        characterId,
        title,
        thumbnail.path,
        thumbnail.extension
    )
}