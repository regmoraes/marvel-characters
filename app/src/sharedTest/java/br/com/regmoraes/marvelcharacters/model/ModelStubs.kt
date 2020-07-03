package br.com.regmoraes.marvelcharacters.model

object ModelStubs {

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

    val comicOne =
        Comic(
            id = 1,
            thumbnail = Thumbnail(path = "https://www.thumb.com/comic-one", extension = ".jpg"),
            title = "Comic One"
        )

    val comicTwo =
        Comic(
            id = 2,
            thumbnail = Thumbnail(path = "https://www.thumb.com/comic-two", extension = ".jpg"),
            title = "Comic Two"
        )
}