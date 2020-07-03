package br.com.regmoraes.marvelcharacters.presentation.model

import android.os.Parcelable
import br.com.regmoraes.marvelcharacters.model.Character
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterParcel(
    val id: Long,
    val thumbnail: ThumbnailParcel,
    val name: String,
    val description: String,
    val resourceURI: String,
    val isFavorite: Boolean = false
) : Parcelable

fun Character.toParcel(): CharacterParcel =
    CharacterParcel(id, thumbnail.toParcel(), name, description, resourceURI, isFavorite)

fun CharacterParcel.toDomain(): Character =
    Character(id, thumbnail.toDomain(), name, description, resourceURI, isFavorite)