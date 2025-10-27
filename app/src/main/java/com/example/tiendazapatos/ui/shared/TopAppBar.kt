package com.example.tiendazapatos.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    navController: NavController,
    cartItemCount: Int,
    userName: String?,
    onLogout: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { /* Required but empty */ },
        actions = {
            if (userName == null) {
                IconButton(onClick = { navController.navigate("login") }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = "Acceder")
                }
            } else {
                Text("Hola, $userName", modifier = Modifier.padding(end = 8.dp))
            }

            BadgedBox(
                badge = { if (cartItemCount > 0) { Badge { Text("$cartItemCount") } } }
            ) {
                IconButton(onClick = { navController.navigate("cart") }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }
            }

            Box {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Más")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text("Administración") }, onClick = { navController.navigate("admin"); showMenu = false })
                    DropdownMenuItem(text = { Text("Historial de Compras") }, onClick = { navController.navigate("orderHistory"); showMenu = false })
                    if (userName != null) {
                        HorizontalDivider()
                        DropdownMenuItem(text = { Text("Cerrar Sesión") }, onClick = { onLogout(); showMenu = false })
                    }
                }
            }
        }
    )
}
