package com.example.tiendazapatos.ui.screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import java.io.File

private fun getTmpFileUri(context: Context): Uri {
    val tmpFile = File.createTempFile("tmp_image_file", ".png", context.cacheDir).apply {
        createNewFile()
        deleteOnExit()
    }
    return FileProvider.getUriForFile(context, "com.example.tiendazapatos.provider", tmpFile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoporteScreen() {
    var message by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri = tempUri
            }
        }
    )

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                imageUri = it
            }
        }
    )

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Atención al Cliente") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("¿Tienes algún problema o consulta?", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Escribe tu mensaje aquí...") },
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { filePickerLauncher.launch("image/*") }) {
                    Icon(Icons.Default.AddLink, contentDescription = "Adjuntar Foto", modifier = Modifier.padding(end = 8.dp))
                    Text("Adjuntar Foto")
                }
                Button(onClick = {
                    val tmpUri = getTmpFileUri(context)
                    tempUri = tmpUri
                    takePictureLauncher.launch(tmpUri)
                }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Tomar Foto", modifier = Modifier.padding(end = 8.dp))
                    Text("Tomar Foto")
                }
            }

            if (imageUri != null) {
                Text("Archivo adjunto: ${imageUri?.lastPathSegment}", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("¡Mensaje enviado con éxito! Te contactaremos pronto.")
                    }
                    message = ""
                    imageUri = null
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = message.isNotBlank()
            ) {
                Text("Enviar Consulta")
            }
        }
    }
}
