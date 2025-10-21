package com.example.tiendazapatos.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController, cartItemCount: Int) {
    var showMenu by remember { mutableStateOf(false) }

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
        },
        actions = {
            // Cart Icon with Badge
            BadgedBox(
                badge = {
                    if (cartItemCount > 0) {
                        Badge { Text("$cartItemCount") }
                    }
                }
            ) {
                IconButton(onClick = { navController.navigate("cart") }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito de compras"
                    )
                }
            }

            // Admin Menu
            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones"
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Administración") },
                        onClick = {
                            navController.navigate("admin")
                            showMenu = false
                        }
                    )
                }
            }
        }
    )
}
