package br.com.regmoraes.marvelcharacters.infrastructure.api

class DataWrapper<T>(
    val code: Int,
    val status: String,
    val data: DataContainer<T>
)

