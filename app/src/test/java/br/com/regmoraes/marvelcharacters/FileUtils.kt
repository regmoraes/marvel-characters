package br.com.regmoraes.marvelcharacters

object FileUtils {
    fun readTestResourceFile(fileName: String): String {
        val fileInputStream = this::class.java.classLoader?.getResourceAsStream(fileName)
        return fileInputStream?.bufferedReader().use { it?.readText() ?: "" }
    }
}