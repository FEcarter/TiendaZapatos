package com.example.tiendazapatos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.R
import com.example.tiendazapatos.data.remote.model.Order
import com.example.tiendazapatos.data.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUri: String,
    val stock: Int = 0
)

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems.asStateFlow()

    val orderHistory: StateFlow<List<Order>> = productRepository.getAllOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

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
        val resourceUriPrefix = "android.resource://com.example.tiendazapatos/"
        _products.value = listOf(
            Product(1, "Zapato Deportivo", "Ideal para correr y entrenar.", 89.99, resourceUriPrefix + R.drawable.zapatodeportivo2, 10),
            Product(2, "Zapato Casual", "Perfecto para el día a día.", 69.99, resourceUriPrefix + R.drawable.zapato1, 20),
            Product(3, "Bota de Cuero", "Elegancia y durabilidad para el invierno.", 129.99, resourceUriPrefix + R.drawable.botadecuero, 15),
            Product(4, "Sandalia de Verano", "Comodidad y frescura para el calor.", 49.99, resourceUriPrefix + R.drawable.zandaliadeverano, 30)
        )
    }

    fun addToCart(product: Product) {
        _cartItems.update { currentCart -> currentCart + product }
    }

    fun removeFromCart(product: Product) {
        _cartItems.update { currentCart -> currentCart - product }
    }

    fun confirmOrder() {
        viewModelScope.launch {
            val currentCart = _cartItems.value
            if (currentCart.isNotEmpty()) {
                val newOrder = Order(
                    total = currentCart.sumOf { it.price },
                    itemCount = currentCart.size
                )
                productRepository.insertOrder(newOrder)
                _cartItems.update { emptyList() }
            }
        }
    }

    fun clearOrderHistory() {
        viewModelScope.launch {
            productRepository.clearAllOrders()
        }
    }

    fun addProduct(name: String, description: String, price: Double, imageUri: String) {
        val newId = (_products.value.maxOfOrNull { it.id } ?: 0) + 1
        val newProduct = Product(
            id = newId,
            name = name,
            description = description,
            price = price,
            imageUri = imageUri,
            stock = 10
        )
        _products.update { currentProducts -> currentProducts + newProduct }
    }

    fun updateProduct(updatedProduct: Product) {
        _products.update { currentProducts ->
            currentProducts.map { product ->
                if (product.id == updatedProduct.id) updatedProduct else product
            }
        }
    }

    fun deleteProduct(productToDelete: Product) {
        _products.update { currentProducts ->
            currentProducts.filter { it.id != productToDelete.id }
        }
    }
}
