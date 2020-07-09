package br.com.regmoraes.marvelcharacters.model


data class Character(
    val id: Long,
    val thumbnail: Thumbnail,
    val name: String,
    val description: String,
    val resourceURI: String,
    val isFavorite: Boolean = false
)