package com.example.tiendazapatos.ui.screen
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.tiendazapatos.data.remote.RetrofitInstance
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Black
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

suspend fun uploadImage(file: File): String? {
    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    val multipart = MultipartBody.Part.createFormData("file", file.name, requestBody)

    val response = RetrofitInstance.apiFiles.uploadFile(multipart)

    return if (response.isSuccessful) {
        response.body()?.data?.url
    } else {
        println("Error: ${response.errorBody()?.string()}")
        null
    }
}

@Composable
fun UploadFileScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var localImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }

    //Launcher para pedir permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                println("Permiso de cámara denegado")
            }
        }
    )

    //Launcher para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            localImageBitmap = it
            val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                it.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            coroutineScope.launch {
                uploadedImageUrl = uploadImage(file)
            }
        }
    }

    //Launcher para elegir desde galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            // Copiar el archivo desde la URI a un archivo temporal
            val inputStream: InputStream? = context.contentResolver.openInputStream(it)
            val file = File(context.cacheDir, "gallery_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            coroutineScope.launch {
                uploadedImageUrl = uploadImage(file)
            }
        }
    }

    //Pedir permiso de cámara al iniciar
    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Subir imagen",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
        }

        item {
            Spacer(Modifier.height(32.dp))

        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                Button(
                    onClick = { takePictureLauncher.launch(null) },
                    colors = ButtonDefaults.buttonColors(containerColor = White),
                ) {
                    Text("Tomar foto", color = Black)
                }

                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = White),
                ) {
                    Text("Elegir de galería", color = Black)
                }
            }
        }

        item {
            //Mostrar imagen local (foto o galería)
            localImageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen capturada",
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        item {

            selectedImageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        item {
            //Mostrar imagen subida desde la URL
            uploadedImageUrl?.let { url ->

                Text(
                    text = "Imagen subida correctamente",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )

                AsyncImage(
                    model = url,
                    contentDescription = url.replace("http://", "https://"),
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = url.replace("http://", "https://"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            }
        }
    }
}







