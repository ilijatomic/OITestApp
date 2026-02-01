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

    companion object {
        const val TAG = "ProductsViewModel"
    }

    private val _productsList = MutableStateFlow<ViewModelState<List<Product>>>(ViewModelState.Result(emptyList()))
    val productsList = _productsList.asStateFlow()

    private val _saveProduct = MutableStateFlow<ViewModelState<Boolean>>(SaveProductState.Idle)
    val saveProduct = _saveProduct.asStateFlow()

    private val _suppliersList = MutableStateFlow<ViewModelState<List<Supplier>>>(ViewModelState.Result(emptyList()))
    val suppliersList = _suppliersList.asStateFlow()

    private var fullList = emptyList<Product>()
    private var searchQuery = ""

    init {
        getProducts()
        getSuppliers()
    }

    fun getProducts() {
        Log.d(TAG, "getProducts")
        viewModelScope.launch {
            _productsList.value = ViewModelState.Loading
            when (val result = getAllProductsUseCase.invoke()) {
                is Resource.Success<*> -> {
                    result.data?.let { fullList = it }
                    updateSearchQuery(searchQuery)
                }
                is Resource.Error<*> -> postError(result, _productsList, TAG)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        Log.d(TAG, "updateSearchQuery: $query")
        searchQuery = query
        val filtered = if (searchQuery.isBlank()) fullList else fullList.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.barcode.contains(query, ignoreCase = true) ||
                it.supplier.name.contains(query, ignoreCase = true)
        }

        _productsList.value = ViewModelState.Result(filtered)
    }

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            _saveProduct.value = ViewModelState.Loading
            when (val result = addEditProductUseCase.invoke(product)) {
                is Resource.Success<*> -> {
                    getProducts()
                    _saveProduct.value = ViewModelState.Result(true)
                }
                is Resource.Error<*> -> postError(result, _saveProduct, TAG)
            }
        }
    }

    fun getSuppliers() {
        Log.d(TAG, "getSuppliers")
        viewModelScope.launch {
            _suppliersList.value = ViewModelState.Loading
            when (val result = getAllSuppliersUseCase.invoke()) {
                is Resource.Success<*> -> _suppliersList.value = ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _suppliersList, TAG)
            }
        }
    }

    fun resetSaveProductState() {
        _saveProduct.value = SaveProductState.Idle
    }

    sealed class SaveProductState : ViewModelState<Boolean>() {
        data object Idle : SaveProductState()
    }
}

