package com.example.iotestapp.ui.suppliers

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.iotestapp.R
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.usecase.login.CheckLoggedInUseCase
import com.example.iotestapp.domain.usecase.login.LoginUseCase
import com.example.iotestapp.domain.usecase.suppliers.AddEditSupplierUseCase
import com.example.iotestapp.domain.usecase.suppliers.GetAllSuppliersUseCase
import com.example.iotestapp.ui.common.BaseViewModel
import com.example.iotestapp.ui.common.ViewModelState
import com.example.iotestapp.ui.login.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuppliersViewModel @Inject constructor(
    private val getAllSuppliersUseCase: GetAllSuppliersUseCase,
    private val addEditSupplierUseCase: AddEditSupplierUseCase,
) : BaseViewModel() {
    companion object {
        const val TAG = "SuppliersViewModel"
    }

    private val _suppliersList = MutableStateFlow<ViewModelState<List<Supplier>>>(ViewModelState.Result(emptyList()))
    val suppliersList = _suppliersList.asStateFlow()

    private var fullList = emptyList<Supplier>()
    private var searchQuery = ""

    init {
        getSuppliers()
    }

    fun getSuppliers() {
        Log.d(TAG, "getSuppliers: ")
        viewModelScope.launch {
            _suppliersList.value = ViewModelState.Loading
            when (val result = getAllSuppliersUseCase.invoke()) {
                is Resource.Success<*> -> {
                    result.data?.let { fullList = it }
                    updateSearchQuery(searchQuery)
                }
                is Resource.Error<*> -> postError(result)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        Log.d(TAG, "updateSearchQuery: $query")
        searchQuery = query
        val filtered = if (searchQuery.isBlank()) fullList else fullList.filter {
            it.name.contains(query, ignoreCase = true)
                    || it.contactPerson.contains(query, ignoreCase = true)
                    || it.email.contains(query, ignoreCase = true)
        }

        _suppliersList.value = ViewModelState.Result(filtered)
    }

    fun saveSupplier(supplier: Supplier) {
        viewModelScope.launch {
            when (val result = addEditSupplierUseCase.invoke(supplier)) {
                is Resource.Success<*> -> {
                    getSuppliers()
                }
                is Resource.Error<*> -> postError(result)
            }
        }
    }

    override fun postError(error: Resource.Error<*>) {
        error.id?.let {
            _suppliersList.value = ViewModelState.Error(it)
        }
        error.message?.let {
            Log.e(LoginViewModel.Companion.TAG, it)
            _suppliersList.value = ViewModelState.Error()
        }
    }

}

