package com.example.tiendazapatos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendazapatos.ui.viewmodel.Product
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    productId: Int
) {
    // Buscar el producto a editar. Si no se encuentra, volver.
    val productToEdit = productViewModel.products.collectAsState().value.find { it.id == productId }

    if (productToEdit == null) {
        // Si el producto no existe, simplemente volvemos.
        // Esto puede pasar si el producto fue eliminado en segundo plano.
        navController.popBackStack()
        return
    }

    // Estados para los valores de los campos, inicializados con los datos del producto
    var name by remember { mutableStateOf(productToEdit.name) }
    var description by remember { mutableStateOf(productToEdit.description) }
    var price by remember { mutableStateOf(productToEdit.price.toString()) }

    // Estados para los mensajes de error de validación
    var nameError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = null },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it; descriptionError = null },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                isError = descriptionError != null,
                supportingText = { descriptionError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it; priceError = null },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = priceError != null,
                supportingText = { priceError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )

            Button(
                onClick = {
                    val priceDouble = price.toDoubleOrNull()
                    var isValid = true

                    if (name.isBlank()) {
                        nameError = "El nombre no puede estar vacío"
                        isValid = false
                    }
                    if (description.isBlank()) {
                        descriptionError = "La descripción no puede estar vacía"
                        isValid = false
                    }
                    if (priceDouble == null || priceDouble <= 0) {
                        priceError = "Introduce un precio válido y mayor que cero"
                        isValid = false
                    }

                    if (isValid) {
                        val updatedProduct = productToEdit.copy(
                            name = name,
                            description = description,
                            price = priceDouble!!
                        )
                        productViewModel.updateProduct(updatedProduct)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}
