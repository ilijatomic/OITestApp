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
    companion object {
        const val TAG = "SuppliersViewModel"
    }

    private val _suppliersList = MutableStateFlow<ViewModelState<List<Supplier>>>(ViewModelState.Result(emptyList()))
    val suppliersList = _suppliersList.asStateFlow()

    private val _saveSupplier = MutableStateFlow<ViewModelState<Boolean>>(SaveSupplierState.Idle)
    val saveSupplier = _saveSupplier.asStateFlow()

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
                is Resource.Error<*> -> postError(result, _suppliersList, TAG)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        Log.d(TAG, "updateSearchQuery: $query")
        searchQuery = query
        val filtered = if (searchQuery.isBlank()) fullList else fullList.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.contactPerson.contains(query, ignoreCase = true) ||
            it.email.contains(query, ignoreCase = true)
        }

        _suppliersList.value = ViewModelState.Result(filtered)
    }

    fun saveSupplier(supplier: Supplier) {
        viewModelScope.launch {
            _saveSupplier.value = ViewModelState.Loading
            when (val result = addEditSupplierUseCase.invoke(supplier)) {
                is Resource.Success<*> -> {
                    _saveSupplier.value = ViewModelState.Result(true)
                    getSuppliers()
                }
                is Resource.Error<*> -> postError(result, _saveSupplier, TAG)
            }
        }
    }

    fun resetSaveSupplierState() {
        _saveSupplier.value = SaveSupplierState.Idle
    }

    sealed class SaveSupplierState : ViewModelState<Boolean>() {
        data object Idle : SaveSupplierState()
    }
}

