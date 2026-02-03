package com.example.iotestapp.ui.suppliers

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.usecase.suppliers.AddEditSupplierUseCase
import com.example.iotestapp.domain.usecase.suppliers.GetAllSuppliersUseCase
import com.example.iotestapp.ui.common.BaseViewModel
import com.example.iotestapp.ui.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuppliersViewModel @Inject constructor(
    private val getAllSuppliersUseCase: GetAllSuppliersUseCase,
    private val addEditSupplierUseCase: AddEditSupplierUseCase,
) : BaseViewModel() {

    private val _suppliersState = MutableStateFlow<ViewModelState<List<Supplier>>>(ViewModelState.Result(emptyList()))
    val suppliersState = _suppliersState.asStateFlow()

    private val _saveSupplierState = MutableStateFlow<ViewModelState<Boolean>>(SaveSupplierState.Idle)
    val saveSupplierState = _saveSupplierState.asStateFlow()

    private var fullList = emptyList<Supplier>()
    private var searchQuery = ""

    fun getSuppliers() {
        viewModelScope.launch {
            _suppliersState.value = ViewModelState.Loading
            val result = getAllSuppliersUseCase.invoke()
            Log.d(TAG, "getSuppliers: $result")
            when (result) {
                is Resource.Success<*> -> {
                    result.data?.let { fullList = it }
                    updateSearchQuery(searchQuery)
                }
                is Resource.Error<*> -> postError(result, _suppliersState, TAG)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        val filtered = if (searchQuery.isBlank()) fullList else fullList.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.contactPerson.contains(query, ignoreCase = true) ||
            it.email.contains(query, ignoreCase = true)
        }
        Log.d(TAG, "updateSearchQuery: $filtered")
        _suppliersState.value = ViewModelState.Result(filtered)
    }

    fun saveSupplier(supplier: Supplier) {
        viewModelScope.launch {
            _saveSupplierState.value = ViewModelState.Loading
            val result = addEditSupplierUseCase.invoke(supplier)
            Log.d(TAG, "saveSupplier: $result")
            when (result) {
                is Resource.Success<*> -> {
                    _saveSupplierState.value = ViewModelState.Result(true)
                    getSuppliers()
                }
                is Resource.Error<*> -> postError(result, _saveSupplierState, TAG)
            }
        }
    }

    fun resetSaveSupplierState() {
        Log.d(TAG, "resetSaveSupplierState: ")
        _saveSupplierState.value = SaveSupplierState.Idle
    }

    sealed class SaveSupplierState : ViewModelState<Boolean>() {
        data object Idle : SaveSupplierState()
    }
}

