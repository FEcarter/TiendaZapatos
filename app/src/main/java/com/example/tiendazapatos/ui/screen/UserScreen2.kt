package com.example.tiendazapatos.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiendazapatos.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen2(viewModel: UserViewModel) {
    val users by viewModel.users.collectAsState()

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Usuarios con Room y Compose 2") })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(all = 16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text(text = "Edad") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
