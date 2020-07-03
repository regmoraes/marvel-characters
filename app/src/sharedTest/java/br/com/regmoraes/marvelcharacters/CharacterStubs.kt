package br.com.regmoraes.marvelcharacters

import br.com.regmoraes.marvelcharacters.model.Character
import br.com.regmoraes.marvelcharacters.model.Thumbnail

object CharacterStubs {

    val characterOne =
        Character(
            id = 1,
            thumbnail = Thumbnail(path = "https://www.thumb.com/hero-one", extension = ".jpg"),
            name = "Hero One",
            description = "The number on hero ",
            resourceURI = "https://www.api.com/hero-one"
        )

    val characterTwo =
        Character(
            id = 2,
            thumbnail = Thumbnail(path = "https://www.thumb.com/hero-two", extension = ".jpg"),
            name = "Hero Two",
            description = "The number two hero",
            resourceURI = "https://www.api.com/hero-two"
        )
}