package com.example.tiendazapatos.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tiendazapatos.R
import com.example.tiendazapatos.ui.components.ProductCard

data class Product(val name: String, val description: String, val price: Double, @DrawableRes val imageRes: Int)

@Composable
fun ProductScreen(modifier: Modifier = Modifier) {
    val products = listOf(
        Product("Zapato Deportivo", "El mejor zapato para correr", 99.99, R.drawable.zapato1),
        Product("Zapato Casual", "Ideal para el día a día", 79.99, R.drawable.zapato1),
        Product("Bota de Cuero", "Estilo y durabilidad", 129.99, R.drawable.zapato1),
        Product("Sandalia de Playa", "Comodidad para el verano", 29.99, R.drawable.zapato1)
    )

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ProductCard(
                name = product.name,
                description = product.description,
                price = product.price,
                imageRes = product.imageRes
            )
        }
    }
}
