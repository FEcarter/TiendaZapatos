package com.example.tiendazapatos.ui.viewmodel

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.R
import com.example.tiendazapatos.data.remote.dao.OrderDao
import com.example.tiendazapatos.data.remote.model.Order
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    @DrawableRes val imageRes: Int
)

class ProductViewModel(private val orderDao: OrderDao) : ViewModel() {

    // --- GESTIÓN DE PRODUCTOS ---
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // --- GESTIÓN DEL CARRITO ---
    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems.asStateFlow()

    // --- GESTIÓN DEL HISTORIAL DE PEDIDOS ---
    val orderHistory: StateFlow<List<Order>> = orderDao.getAllOrders()
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
        _products.value = listOf(
            Product(1, "Zapato Deportivo", "Ideal para correr y entrenar.", 89.99, R.drawable.zapatodeportivo2),
            Product(2, "Zapato Casual", "Perfecto para el día a día.", 69.99, R.drawable.zapato1),
            Product(3, "Bota de Cuero", "Elegancia y durabilidad para el invierno.", 129.99, R.drawable.botadecuero),
            Product(4, "Sandalia de Verano", "Comodidad y frescura para el calor.", 49.99, R.drawable.zandaliadeverano)
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
        viewModelScope.launch {
            val currentCart = _cartItems.value
            if (currentCart.isNotEmpty()) {
                val newOrder = Order(
                    total = currentCart.sumOf { it.price },
                    itemCount = currentCart.size
                )
                orderDao.insertOrder(newOrder)
                _cartItems.update { emptyList() } // Vaciar el carrito después de guardar
            }
        }
    }

    fun clearOrderHistory() {
        viewModelScope.launch {
            orderDao.clearAllOrders()
        }
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
