package com.example.tiendazapatos.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel

@Composable
fun CartScreen(productViewModel: ProductViewModel = viewModel()) {
    val cartItems by productViewModel.cartItems.collectAsState()
    val totalPrice by productViewModel.totalPrice.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Carrito de Compras", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        if (cartItems.isEmpty()) {
            Text("Tu carrito está vacío.", modifier = Modifier.padding(top = 16.dp))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(cartItems) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(product.name, modifier = Modifier.weight(1f))
                        Text(text = "$%.2f".format(product.price), modifier = Modifier.padding(horizontal = 16.dp))
                        IconButton(onClick = { productViewModel.removeFromCart(product) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar producto")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider() // Un separador visual
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("$%.2f".format(totalPrice), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
