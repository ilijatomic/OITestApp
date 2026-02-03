package com.example.iotestapp.ui.dashboard

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.iotestapp.domain.common.Resource
import com.example.iotestapp.domain.model.Product
import com.example.iotestapp.domain.usecase.dashboard.GetLowStockProductsUseCase
import com.example.iotestapp.domain.usecase.dashboard.GetRecentTransactionsUseCase
import com.example.iotestapp.ui.common.BaseViewModel
import com.example.iotestapp.ui.common.ViewModelState
import com.example.testapp.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getLowStockProductsUseCase: GetLowStockProductsUseCase,
    private val getRecentTransactionsUseCase: GetRecentTransactionsUseCase,
) : BaseViewModel() {

    private val _lowStockProductsState = MutableStateFlow<ViewModelState<List<Product>>>(ViewModelState.Loading)
    val lowStockProductsState = _lowStockProductsState.asStateFlow()

    private val _recentTransactionsState = MutableStateFlow<ViewModelState<List<Transaction>>>(ViewModelState.Loading)
    val recentTransactionsState = _recentTransactionsState.asStateFlow()

    fun loadLowStockProducts() {
        viewModelScope.launch {
            _lowStockProductsState.value = ViewModelState.Loading
            val result = getLowStockProductsUseCase.invoke()
            Log.d(TAG, "loadLowStockProducts: $result")
            when (result) {
                is Resource.Success<*> -> _lowStockProductsState.value =
                    ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _lowStockProductsState, TAG)
            }
        }
    }

    fun loadRecentTransactions() {
        viewModelScope.launch {
            _recentTransactionsState.value = ViewModelState.Loading
            val result = getRecentTransactionsUseCase.invoke()
            Log.d(TAG, "loadRecentTransactions: $result")
            when (result) {
                is Resource.Success<*> -> _recentTransactionsState.value =
                    ViewModelState.Result(result.data ?: emptyList())
                is Resource.Error<*> -> postError(result, _recentTransactionsState, TAG)
            }
        }
    }
}

