package com.example.tiendazapatos.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido a Zaretti",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "En Zaretti, creemos que un par de zapatos puede cambiar tu vida. Desde 1985, nos dedicamos a ofrecer calzado de la más alta calidad, combinando la artesanía tradicional con las últimas tendencias de la moda. Cada par está diseñado pensando en la comodidad, la elegancia y la durabilidad. Descubre una colección donde cada paso cuenta y encuentra el zapato perfecto que habla de ti.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate("producto") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver nuestros productos")
        }
    }
}
