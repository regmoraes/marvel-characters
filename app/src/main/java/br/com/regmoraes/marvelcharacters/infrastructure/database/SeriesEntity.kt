package br.com.regmoraes.marvelcharacters.infrastructure.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.regmoraes.marvelcharacters.model.Series
import br.com.regmoraes.marvelcharacters.model.Thumbnail

@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey val id: Long,
    val characterId: Long,
    val title: String,
    val thumbnailPath: String,
    val thumbnailExtension: String
)

fun SeriesEntity.toSeries(): Series {
    return Series(
        id,
        title,
        Thumbnail(thumbnailPath, thumbnailExtension)
    )
}

fun Series.toEntity(characterId: Long): SeriesEntity {
    return SeriesEntity(
        id,
        characterId,
        title,
        thumbnail.path,
        thumbnail.extension
    )
}