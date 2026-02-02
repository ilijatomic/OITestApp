package com.example.iotestapp.ui.transactions

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

    private val _transactionsState = MutableStateFlow<ViewModelState<List<Transaction>>>(ViewModelState.Result(emptyList()))
    val transactionsState = _transactionsState.asStateFlow()

    private val _saveTransactionState = MutableStateFlow<ViewModelState<Boolean>>(SaveTransactionState.Idle)
    val saveTransactionState = _saveTransactionState.asStateFlow()

    private val _productsState = MutableStateFlow<ViewModelState<List<Product>>>(ViewModelState.Result(emptyList()))
    val productsState = _productsState.asStateFlow()

    init {
        getTransactions()
        getProducts()
    }

    fun getTransactions() {
        viewModelScope.launch {
            _transactionsState.value = ViewModelState.Loading
            when (val result = getAllTransactionsUseCase.invoke()) {
                is Resource.Success<*> -> _transactionsState.value =
                    ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _transactionsState, TAG)
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
        viewModelScope.launch {
            _productsState.value = ViewModelState.Loading
            when (val result = getAllProductsUseCase.invoke()) {
                is Resource.Success<*> -> _productsState.value = ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _productsState, TAG)
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

