package com.example.tiendazapatos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendazapatos.data.model.Product
import com.example.tiendazapatos.data.remote.model.Order
import com.example.tiendazapatos.data.repository.ProductRepositoryInterface
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepositoryInterface) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems.asStateFlow()

    val orderHistory: StateFlow<List<Order>> = productRepository.getAllOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L), // Esta política está bien para la BBDD
            initialValue = emptyList()
        )

    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.price }
    }.stateIn(
        scope = viewModelScope,
        // CORRECCIÓN: Se cambia a Eagerly para que el flujo esté siempre activo y sea fácil de probar.
        started = SharingStarted.Eagerly,
        initialValue = 0.0
    )

    init {
        loadProductsFromApi()
    }

    private fun loadProductsFromApi() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _products.value = productRepository.getProducts()
            } catch (e: Exception) {
                _error.value = "Error al cargar los productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(product: Product) {
        _cartItems.update { currentCart -> currentCart + product }
    }

    fun removeFromCart(product: Product) {
        _cartItems.update { currentCart -> currentCart.minus(product) }
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
        val newProduct = Product(id = newId, name = name, description = description, price = price, imageUri = imageUri, stock = 10)
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
