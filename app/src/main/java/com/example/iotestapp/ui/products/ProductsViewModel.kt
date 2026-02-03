package com.example.iotestapp.ui.products

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.model.Supplier
import com.example.iotestapp.domain.usecase.products.AddEditProductUseCase
import com.example.iotestapp.domain.usecase.products.GetAllProductsUseCase
import com.example.iotestapp.domain.usecase.suppliers.GetAllSuppliersUseCase
import com.example.iotestapp.ui.common.BaseViewModel
import com.example.iotestapp.ui.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addEditProductUseCase: AddEditProductUseCase,
    private val getAllSuppliersUseCase: GetAllSuppliersUseCase,
) : BaseViewModel() {

    private val _productsState = MutableStateFlow<ViewModelState<List<Product>>>(ViewModelState.Result(emptyList()))
    val productsState = _productsState.asStateFlow()

    private val _saveProductState = MutableStateFlow<ViewModelState<Boolean>>(SaveProductState.Idle)
    val saveProductState = _saveProductState.asStateFlow()

    private val _suppliersState = MutableStateFlow<ViewModelState<List<Supplier>>>(ViewModelState.Result(emptyList()))
    val suppliersState = _suppliersState.asStateFlow()

    private var fullList = emptyList<Product>()
    private var searchQuery = ""

    fun getProducts() {
        viewModelScope.launch {
            _productsState.value = ViewModelState.Loading
            val result = getAllProductsUseCase.invoke()
            Log.d(TAG, "getProducts: $result")
            when (result) {
                is Resource.Success<*> -> {
                    result.data?.let { fullList = it }
                    updateSearchQuery(searchQuery)
                }
                is Resource.Error<*> -> postError(result, _productsState, TAG)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        val filtered = if (searchQuery.isBlank()) fullList else fullList.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.barcode.contains(query, ignoreCase = true) ||
                it.supplier.name.contains(query, ignoreCase = true)
        }
        Log.d(TAG, "updateSearchQuery: $filtered")
        _productsState.value = ViewModelState.Result(filtered)
    }

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            _saveProductState.value = ViewModelState.Loading
            val result = addEditProductUseCase.invoke(product)
            Log.d(TAG, "saveProduct: $result")
            when (result) {
                is Resource.Success<*> -> {
                    getProducts()
                    _saveProductState.value = ViewModelState.Result(true)
                }
                is Resource.Error<*> -> postError(result, _saveProductState, TAG)
            }
        }
    }

    fun getSuppliers() {
        viewModelScope.launch {
            _suppliersState.value = ViewModelState.Loading
            val result = getAllSuppliersUseCase.invoke()
            Log.d(TAG, "getSuppliers: $result")
            when (result) {
                is Resource.Success<*> -> _suppliersState.value = ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _suppliersState, TAG)
            }
        }
    }

    fun resetSaveProductState() {
        Log.d(TAG, "resetSaveProductState: ")
        _saveProductState.value = SaveProductState.Idle
    }

    sealed class SaveProductState : ViewModelState<Boolean>() {
        data object Idle : SaveProductState()
    }
}

