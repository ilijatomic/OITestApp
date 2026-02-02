package com.example.iotestapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimen(
    val navDividerHeight: Dp = 8.dp,
    val navDrawerHeaderSize: Dp = 64.dp,
    val navDrawerHeaderPaddingV: Dp = 24.dp,
    val navDrawerHeaderPaddingH: Dp = 16.dp,
    val loginPaddingB: Dp = 220.dp,
    val dashboardPadding: Dp = 16.dp,
    val supplierPadding: Dp = 16.dp,
    val supplierFabPadding: Dp = 32.dp,
    val supplierListPaddingB: Dp = 80.dp,
    val supplierListSpace: Dp = 8.dp,
    val supplierListEmptyPadding: Dp = 24.dp,
    val supplierListItemPadding: Dp = 16.dp,
    val supplierAddDialogPadding: Dp = 16.dp,
    val supplierAddDialogShapeSize: Dp = 16.dp,
    val productPadding: Dp = 16.dp,
    val productFabPadding: Dp = 32.dp,
    val productListPaddingB: Dp = 80.dp,
    val productListSpace: Dp = 8.dp,
    val productListEmptyPadding: Dp = 24.dp,
    val productListItemPadding: Dp = 16.dp,
    val productAddDialogPadding: Dp = 16.dp,
    val productAddDialogShapeSize: Dp = 16.dp,
    val transactionPadding: Dp = 16.dp,
    val transactionFabPadding: Dp = 32.dp,
    val transactionFilterPadding: Dp = 12.dp,
    val transactionListPaddingB: Dp = 80.dp,
    val transactionListSpace: Dp = 8.dp,
    val transactionListEmptyPadding: Dp = 24.dp,
    val transactionListItemPadding: Dp = 16.dp,
    val transactionAddDialogPadding: Dp = 16.dp,
    val transactionAddDialogShapeSize: Dp = 16.dp,
)

val LocalDimen = staticCompositionLocalOf { Dimen() }

val MaterialTheme.dimen: Dimen
    @Composable get() = LocalDimen.current