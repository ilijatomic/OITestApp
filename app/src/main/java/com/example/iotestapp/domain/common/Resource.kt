package com.example.iotestapp.domain.common

import androidx.annotation.StringRes

sealed class Resource<T>(
    open val data: T? = null,
    @StringRes open val id: Int? = null,
    open val message: String? = null,
) {
    data class Success<T>(override val data: T) : Resource<T>()
    data class Error<T>(@StringRes override val id: Int? = null, override val message: String? = null) : Resource<T>()
}