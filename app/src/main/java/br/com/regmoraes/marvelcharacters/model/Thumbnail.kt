package br.com.regmoraes.marvelcharacters.model

data class Thumbnail(val path: String, val extension: String) {
    val pathWithExtension: String
        get() {
            val httpsPath = path.replaceFirst("http:", "https:")
            return "$httpsPath.$extension"
        }
}