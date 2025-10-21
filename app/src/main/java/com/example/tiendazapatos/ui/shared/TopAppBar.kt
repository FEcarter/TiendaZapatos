package com.example.tiendazapatos.ui.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { navController.navigate("inicio") }) {
                    Text("Inicio")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { navController.navigate("cliente") }) {
                    Text("Cliente")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { navController.navigate("producto") }) {
                    Text("Producto")
                }
            }
        }
    )
}
