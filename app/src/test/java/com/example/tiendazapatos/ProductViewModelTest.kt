package com.example.tiendazapatos

import com.example.tiendazapatos.data.model.Product
import com.example.tiendazapatos.data.remote.model.Order
import com.example.tiendazapatos.data.repository.ProductRepositoryInterface
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeProductRepository : ProductRepositoryInterface {
    var insertOrderCalled = false // Bandera para verificar si se llamó al método
        private set

    override suspend fun getProducts(): List<Product> = emptyList()
    override fun getAllOrders(): Flow<List<Order>> = flowOf(emptyList())
    override suspend fun insertOrder(order: Order) {
        insertOrderCalled = true // Marcamos la bandera cuando se llama
    }
    override suspend fun clearAllOrders() {}
}

@ExperimentalCoroutinesApi
class ProductViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ProductViewModel
    private lateinit var fakeRepository: FakeProductRepository // Usamos la clase concreta para acceder a la bandera

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeProductRepository()
        viewModel = ProductViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addProductToCart_shouldContainTheProduct() = runTest {
        val product = Product(1, "Zapato de Prueba", "", 99.99, "", 1)
        viewModel.addToCart(product)
        val cart = viewModel.cartItems.first()
        assertEquals(1, cart.size)
        assertEquals("Zapato de Prueba", cart.first().name)
    }

    @Test
    fun addMultipleProductsToCart_shouldUpdateTotalPriceCorrectly() = runTest {
        val product1 = Product(1, "Zapato A", "", 100.0, "", 1)
        val product2 = Product(2, "Zapato B", "", 50.50, "", 1)

        viewModel.addToCart(product1)
        viewModel.addToCart(product2)

        val actualTotalPrice = viewModel.totalPrice.value
        assertEquals(150.50, actualTotalPrice, 0.001)
    }

    @Test
    fun removeProductFromCart_shouldUpdateTotalPriceCorrectly() = runTest {
        val product1 = Product(1, "Zapato A", "", 100.0, "", 1)
        val product2 = Product(2, "Zapato B", "", 50.50, "", 1)

        viewModel.addToCart(product1)
        viewModel.addToCart(product2)
        viewModel.removeFromCart(product2)

        val actualTotalPrice = viewModel.totalPrice.value
        assertEquals(100.0, actualTotalPrice, 0.001)
    }

    @Test
    fun confirmOrder_shouldClearCartAndResetPrice() = runTest {
        // Arrange
        val product = Product(1, "Zapato a Comprar", "", 123.45, "", 1)
        viewModel.addToCart(product)

        // Act
        viewModel.confirmOrder()

        // Assert
        assertTrue("El carrito debería estar vacío después de confirmar la orden", viewModel.cartItems.value.isEmpty())
        assertEquals("El precio total debería ser 0.0 después de confirmar la orden", 0.0, viewModel.totalPrice.value, 0.001)
        assertTrue("Se debería haber llamado a insertOrder en el repositorio", fakeRepository.insertOrderCalled)
    }
}
