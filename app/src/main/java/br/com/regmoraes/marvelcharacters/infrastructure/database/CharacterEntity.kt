package br.com.regmoraes.marvelcharacters.infrastructure.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.Thumbnail

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Long,
    val thumbnailPath: String,
    val thumbnailExtension: String,
    val name: String,
    val description: String,
    val resourceURI: String
)

fun CharacterEntity.toCharacter(): Character {
    return Character(
        id,
        Thumbnail(thumbnailPath, thumbnailExtension),
        name,
        description,
        resourceURI
    )
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id,
        thumbnail.path,
        thumbnail.extension,
        name,
        description,
        resourceURI
    )
}