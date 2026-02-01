package com.example.iotestapp.ui.common

import androidx.lifecycle.ViewModel
import com.example.iotestapp.domain.common.Resource

abstract class BaseViewModel : ViewModel() {
    protected open fun postError(error: Resource.Error<*>) {}
}