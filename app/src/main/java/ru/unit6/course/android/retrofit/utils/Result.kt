package ru.unit6.course.android.retrofit.utils

sealed class Result<out T : Any?> {
    object NoInternet : Result<Nothing>()

    data class Success<out T : Any>(val value: T?) : Result<T>()

    data class Failure(val errorMessage: String? = null) : Result<Nothing>()
}