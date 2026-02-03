package com.example.iotestapp.ui.common

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.iotestapp.domain.common.Resource
import kotlinx.coroutines.flow.MutableStateFlow

open class BaseViewModel : ViewModel() {

    protected val TAG: String = this::class.java.simpleName

    protected fun <T> postError(
        error: Resource.Error<*>,
        state: MutableStateFlow<ViewModelState<T>>,
        tag: String = TAG,
    ) {

        error.id?.let { state.value = ViewModelState.Error(it) }
        error.message?.let {
            Log.e(tag, it)
            state.value = ViewModelState.Error()
        }
    }
}