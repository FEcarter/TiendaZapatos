package com.example.tiendazapatos.ui.viewmodel

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

// Se movió desde ProductScreen.kt para ser gestionado centralmente
data class Product(
    val id: Int, // Añadimos un ID único para cada producto
    val name: String,
    val description: String,
    val price: Double,
    @DrawableRes val imageRes: Int
)

class ProductViewModel : ViewModel() {

    // --- GESTIÓN DE PRODUCTOS ---
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // --- GESTIÓN DEL CARRITO ---
    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems.asStateFlow()

    // --- PRECIO TOTAL DEL CARRITO (Calculado automáticamente a partir de cartItems) ---
    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.price }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = 0.0
    )


    init {
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = listOf(
            Product(1, "Zapato Deportivo", "Ideal para correr y entrenar.", 89.99, R.drawable.zapato1),
            Product(2, "Zapato Casual", "Perfecto para el día a día.", 69.99, R.drawable.zapato1),
            Product(3, "Bota de Cuero", "Elegancia y durabilidad para el invierno.", 129.99, R.drawable.zapato1),
            Product(4, "Sandalia de Verano", "Comodidad y frescura para el calor.", 49.99, R.drawable.zapato1)
        )
    }

    // --- FUNCIONES DEL CARRITO ---

    fun addToCart(product: Product) {
        _cartItems.update { currentCart -> currentCart + product }
    }

    fun removeFromCart(product: Product) {
        _cartItems.update { currentCart -> currentCart - product }
    }

    fun confirmOrder() {
        _cartItems.update { emptyList() }
    }

    // --- FUNCIONES DE ADMINISTRADOR (CRUD) ---

    fun addProduct(name: String, description: String, price: Double) {
        val newId = (_products.value.maxOfOrNull { it.id } ?: 0) + 1
        val newProduct = Product(
            id = newId,
            name = name,
            description = description,
            price = price,
            imageRes = R.drawable.zapato1 // Usamos una imagen por defecto
        )
        _products.update { currentProducts -> currentProducts + newProduct }
    }

    fun updateProduct(updatedProduct: Product) {
        _products.update { currentProducts ->
            currentProducts.map { product ->
                if (product.id == updatedProduct.id) {
                    updatedProduct
                } else {
                    product
                }
            }
        }
    }

    fun deleteProduct(productToDelete: Product) {
        _products.update { currentProducts ->
            currentProducts.filter { it.id != productToDelete.id }
        }
    }
}
