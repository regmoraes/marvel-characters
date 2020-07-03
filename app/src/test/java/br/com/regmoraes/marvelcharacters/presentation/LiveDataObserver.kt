package br.com.regmoraes.marvelcharacters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * ViewModel Observer test utility class that stores emitted values from a ViewModel
 * and allow the verification of these values.
 *
 * From https://medium.com/@nicolas.duponchel/testing-viewmodel-in-mvvm-using-livedata-and-rxjava-b27878495220
 */
class TestObserver<T> : Observer<T> {

    private val _observedValues = mutableListOf<T?>()
    val observedValues: List<T?> get() = _observedValues

    override fun onChanged(value: T?) {
        _observedValues.add(value)
    }
}

/**
 * ViewModel Observer extension fuction to easily create a TestObserver
 *
 * From https://medium.com/@nicolas.duponchel/testing-viewmodel-in-mvvm-using-livedata-and-rxjava-b27878495220
 */
fun <T> LiveData<T>.testObserver() = TestObserver<T>()
    .also {
        observeForever(it)
    }