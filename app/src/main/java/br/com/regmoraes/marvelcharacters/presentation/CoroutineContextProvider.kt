package br.com.regmoraes.marvelcharacters.presentation

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class CoroutineContextProvider(
    val main: CoroutineContext = Dispatchers.Main,
    val io: CoroutineContext = Dispatchers.IO
)