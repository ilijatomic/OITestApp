package com.example.iotestapp.ui.transactions

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.usecase.products.GetAllProductsUseCase
import com.example.iotestapp.domain.usecase.transactions.AddTransactionUseCase
import com.example.iotestapp.domain.usecase.transactions.GetAllTransactionsUseCase
import com.example.iotestapp.ui.common.BaseViewModel
import com.example.iotestapp.ui.common.ViewModelState
import com.example.testapp.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
) : BaseViewModel() {

    companion object {
        const val TAG = "TransactionsViewModel"
    }

    private val _transactionsList = MutableStateFlow<ViewModelState<List<Transaction>>>(ViewModelState.Result(emptyList()))
    val transactionsList = _transactionsList.asStateFlow()

    private val _saveTransactionState = MutableStateFlow<ViewModelState<Boolean>>(SaveTransactionState.Idle)
    val saveTransactionState = _saveTransactionState.asStateFlow()

    private val _productsList = MutableStateFlow<ViewModelState<List<Product>>>(ViewModelState.Result(emptyList()))
    val productsList = _productsList.asStateFlow()

    init {
        getTransactions()
        getProducts()
    }

    fun getTransactions() {
        Log.d(TAG, "getTransactions")
        viewModelScope.launch {
            _transactionsList.value = ViewModelState.Loading
            when (val result = getAllTransactionsUseCase.invoke()) {
                is Resource.Success<*> -> _transactionsList.value =
                    ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _transactionsList, TAG)
            }
        }
    }

    fun saveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _saveTransactionState.value = ViewModelState.Loading
            when (val result = addTransactionUseCase.invoke(transaction)) {
                is Resource.Success<*> -> {
                    getTransactions()
                    _saveTransactionState.value = ViewModelState.Result(true)
                }
                is Resource.Error<*> -> postError(result, _saveTransactionState, TAG)
            }
        }
    }

    fun getProducts() {
        Log.d(TAG, "getProducts")
        viewModelScope.launch {
            _productsList.value = ViewModelState.Loading
            when (val result = getAllProductsUseCase.invoke()) {
                is Resource.Success<*> -> _productsList.value = ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _productsList, TAG)
            }
        }
    }

    fun resetSaveTransactionState() {
        _saveTransactionState.value = SaveTransactionState.Idle
    }

    sealed class SaveTransactionState : ViewModelState<Boolean>() {
        data object Idle : SaveTransactionState()
    }
}

