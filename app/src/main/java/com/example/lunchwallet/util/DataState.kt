package com.example.lunchwallet.util

sealed class DataState<out R> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error<T>(val errorMessage: String) : DataState<T>()
    object Loading : DataState<Nothing>()

}
