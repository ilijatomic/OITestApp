package com.example.iotestapp.domain.common

import androidx.annotation.StringRes

sealed class Resource<T>(
    val data: T? = null,
    @StringRes val id: Int? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(@StringRes id: Int) : Resource<T>(id = id)
    class Exception<T>(message: String) : Resource<T>(message = message)
}