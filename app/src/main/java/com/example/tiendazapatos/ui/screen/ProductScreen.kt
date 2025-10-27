package com.example.tiendazapatos.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendazapatos.ui.components.ProductCard
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = viewModel(),
    navController: NavController
) {
    val products by productViewModel.products.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ProductCard(
                name = product.name,
                price = product.price,
                imageUri = product.imageUri,
                onAddToCartClick = { productViewModel.addToCart(product) },
                onCardClick = { navController.navigate("productDetail/${product.id}") }
            )
        }
    }
}
