package com.example.tiendazapatos.ui.screen

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Buzón de Cliente",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Déjanos tu queja, sugerencia o felicitación.",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Campo de Correo
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Tu Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                singleLine = true,
                supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Mensaje
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Tu Mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Enviar
            Button(
                onClick = {
                    // Validar email
                    if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Por favor, introduce un correo válido."
                    } else if (message.isBlank()) {
                        // (Opcional) Podríamos añadir validación para el mensaje
                        scope.launch {
                            snackbarHostState.showSnackbar("El mensaje no puede estar vacío.")
                        }
                    } else {
                        // Lógica de envío
                        scope.launch {
                            snackbarHostState.showSnackbar("¡Mensaje enviado! Gracias por tu feedback.")
                        }
                        // Limpiar campos
                        email = ""
                        message = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar Mensaje")
            }
        }
    }
}
