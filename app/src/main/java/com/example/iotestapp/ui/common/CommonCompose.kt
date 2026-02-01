package com.example.iotestapp.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacerSmall() = Spacer(modifier = Modifier.height(4.dp))

@Composable
fun HorizontalSpacerMedium() = Spacer(modifier = Modifier.height(8.dp))

@Composable
fun HorizontalSpacerLarge() = Spacer(modifier = Modifier.height(12.dp))