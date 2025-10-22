package com.example.tiendazapatos.ui.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendazapatos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    navController: NavController,
    cartItemCount: Int,
    userName: String?,
    onLogout: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { navController.navigate("inicio") }) { Text("Inicio") }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { navController.navigate("cliente") }) { Text("Cliente") }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { navController.navigate("producto") }) { Text("Producto") }
            }
        },
        navigationIcon = {
             IconButton(onClick = { navController.navigate("inicio") }) {
                Image(
                    painter = painterResource(id = R.drawable.zaretti),
                    contentDescription = "Logo de Zaretti",
                    modifier = Modifier.height(32.dp)
                )
            }
        },
        actions = {
            // Si el usuario no ha iniciado sesión, mostrar botón para acceder
            if (userName == null) {
                TextButton(onClick = { navController.navigate("login") }) {
                    Text("Acceder")
                }
            } else {
                // Si ha iniciado sesión, mostrar su nombre
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
                        Divider()
                        DropdownMenuItem(text = { Text("Cerrar Sesión") }, onClick = { onLogout(); showMenu = false })
                    }
                }
            }
        }
    )
}
