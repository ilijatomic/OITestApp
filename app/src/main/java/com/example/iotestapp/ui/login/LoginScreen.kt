package com.example.iotestapp.ui.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.iotestapp.R
import kotlin.math.log

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (loginState) {
            is LoginViewModel.State.Checking -> {
                CircularProgressIndicator()
            }

            is LoginViewModel.State.Success -> {
                Log.d("ilija", "success")
                onLoginSuccess()
            }

            else -> {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(stringResource(R.string.username)) },
                    enabled = loginState !is LoginViewModel.State.Loading
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = loginState !is LoginViewModel.State.Loading
                )
                if (loginState is LoginViewModel.State.Error) {
                    (loginState as LoginViewModel.State.Error).id?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(it),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.loginUser(username, password) }
                ) {
                    if (loginState is LoginViewModel.State.Loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                    } else {
                        Text(stringResource(R.string.login))
                    }
                }
            }
        }
    }
}