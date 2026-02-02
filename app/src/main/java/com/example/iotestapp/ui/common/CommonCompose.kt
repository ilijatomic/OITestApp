package com.example.iotestapp.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HorizontalSpacerSmall() = Spacer(modifier = Modifier.height(4.dp))

@Composable
fun HorizontalSpacerMedium() = Spacer(modifier = Modifier.height(8.dp))

@Composable
fun HorizontalSpacerLarge() = Spacer(modifier = Modifier.height(12.dp))

@Composable
fun VerticalSpacerSmall() = Spacer(modifier = Modifier.width(4.dp))

@Composable
fun VerticalSpacerMedium() = Spacer(modifier = Modifier.width(8.dp))

@Composable
fun VerticalSpacerLarge() = Spacer(modifier = Modifier.width(12.dp))

@Composable
fun formattedDate(epochMillis: Long): String {
    val formatter = remember(epochMillis) { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    return formatter.format(Date(epochMillis))
}
