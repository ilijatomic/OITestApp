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

    private val _transactionsState = MutableStateFlow<ViewModelState<List<Transaction>>>(ViewModelState.Result(emptyList()))
    val transactionsState = _transactionsState.asStateFlow()

    private val _saveTransactionState = MutableStateFlow<ViewModelState<Transaction>>(SaveTransactionState.Idle)
    val saveTransactionState = _saveTransactionState.asStateFlow()

    private val _productsState = MutableStateFlow<ViewModelState<List<Product>>>(ViewModelState.Result(emptyList()))
    val productsState = _productsState.asStateFlow()

    fun getTransactions() {
        viewModelScope.launch {
            _transactionsState.value = ViewModelState.Loading
            val result = getAllTransactionsUseCase.invoke()
            Log.d(TAG, "getTransactions: $result")
            when (result) {
                is Resource.Success<*> -> _transactionsState.value =
                    ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _transactionsState, TAG)
            }
        }
    }

    fun saveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _saveTransactionState.value = ViewModelState.Loading
            val result = addTransactionUseCase.invoke(transaction)
            Log.d(TAG, "saveTransaction: $result")
            when (result) {
                is Resource.Success<*> -> {
                    getTransactions()
                    result.data?.let {
                        _saveTransactionState.value = ViewModelState.Result(it)
                    }
                }
                is Resource.Error<*> -> postError(result, _saveTransactionState, TAG)
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            _productsState.value = ViewModelState.Loading
            val result = getAllProductsUseCase.invoke()
            Log.d(TAG, "getProducts: $result")
            when (result) {
                is Resource.Success<*> -> _productsState.value = ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _productsState, TAG)
            }
        }
    }

    fun resetSaveTransactionState() {
        Log.d(TAG, "resetSaveTransactionState: ")
        _saveTransactionState.value = SaveTransactionState.Idle
    }

    sealed class SaveTransactionState : ViewModelState<Transaction>() {
        data object Idle : SaveTransactionState()
    }
}

