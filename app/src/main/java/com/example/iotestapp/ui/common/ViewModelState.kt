package com.example.iotestapp.ui.common

import androidx.annotation.StringRes
import com.example.iotestapp.R

abstract class ViewModelState<out T> {
    object Loading : ViewModelState<Nothing>()
    data class Result<T>(val data: T) : ViewModelState<T>()
    data class Error(@StringRes val id: Int? = R.string.exception_error) : ViewModelState<Nothing>()
}