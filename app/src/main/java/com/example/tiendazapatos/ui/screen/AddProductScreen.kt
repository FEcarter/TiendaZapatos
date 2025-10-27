package com.example.tiendazapatos.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    productViewModel: ProductViewModel
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    imageUri = it
                    imageError = null
                } catch (e: SecurityException) {
                    e.printStackTrace()
                    imageError = "No se pudo obtener permiso para la imagen."
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Producto") },
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

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }
                Spacer(modifier = Modifier.width(16.dp))

                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Vista previa de la imagen",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.size(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sin imagen")
                    }
                }
            }
            if (imageError != null) {
                Text(imageError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = {
                    val priceDouble = price.toDoubleOrNull()
                    var isValid = true

                    if (name.isBlank()) { nameError = "El nombre no puede estar vacío"; isValid = false }
                    if (description.isBlank()) { descriptionError = "La descripción no puede estar vacía"; isValid = false }
                    if (priceDouble == null || priceDouble <= 0) { priceError = "Introduce un precio válido"; isValid = false }
                    if (imageUri == null) { imageError = "Debes seleccionar una imagen"; isValid = false }

                    if (isValid) {
                        productViewModel.addProduct(name, description, priceDouble!!, imageUri.toString())
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Producto")
            }
        }
    }
}
