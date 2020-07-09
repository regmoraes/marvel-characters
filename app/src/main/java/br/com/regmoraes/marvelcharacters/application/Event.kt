package br.com.regmoraes.marvelcharacters.application

sealed class Event<out S> {
    data class Success<out S>(val data: S) : Event<S>()
    data class Error(val error: Throwable) : Event<Nothing>()

    companion object {
        fun <S> success(value: S): Event<S> = Success(value)
        fun error(value: Throwable): Event<Nothing> = Error(value)

        fun <T, U> Event<T>.handle(onSuccess: (T) -> U, onError: ((Throwable) -> U)): U =
            when (this) {
                is Success -> onSuccess(this.data)
                is Error -> onError(this.error)
            }

        fun <T, U> Event<T>.map(transform: (T) -> U): Event<U> =
            when (this) {
                is Success -> flatMap { success(transform(data)) }
                is Error -> this
            }

        fun <T, U> Event<T>.flatMap(transform: (T) -> Event<U>): Event<U> =
            when (this) {
                is Success -> transform(data)
                is Error -> this
            }
    }
}