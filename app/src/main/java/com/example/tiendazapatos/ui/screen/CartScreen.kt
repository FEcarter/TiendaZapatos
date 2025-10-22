package com.example.tiendazapatos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(productViewModel: ProductViewModel = viewModel()) {
    val cartItems by productViewModel.cartItems.collectAsState()
    val totalPrice by productViewModel.totalPrice.collectAsState()

    // Para gestionar el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                BottomAppBar {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total: $%.2f".format(totalPrice), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Button(
                            onClick = {
                                productViewModel.confirmOrder()
                                scope.launch {
                                    snackbarHostState.showSnackbar("¡Comprado con éxito!")
                                }
                            }
                        ) {
                            Text("Comprar")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Carrito de Compras", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(cartItems) { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(product.name, modifier = Modifier.weight(1f))
                            Text(text = "$%.2f".format(product.price), modifier = Modifier.padding(horizontal = 16.dp))
                            IconButton(onClick = { productViewModel.removeFromCart(product) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar producto")
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
