package com.example.tiendazapatos

import com.example.tiendazapatos.data.remote.model.User
import com.example.tiendazapatos.data.repository.UserRepositoryInterface
import com.example.tiendazapatos.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FakeUserRepository : UserRepositoryInterface {
    private val users = mutableListOf<User>()

    override suspend fun getUserByName(name: String): User? {
        return users.find { it.name == name }
    }

    override suspend fun insertUser(user: User) {
        users.add(user)
    }

    // Función de ayuda para nuestras pruebas
    fun countUsers(): Int = users.size
}

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AuthViewModel
    // Cambiamos el tipo a la clase concreta para acceder a la función de ayuda
    private lateinit var fakeRepository: FakeUserRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeUserRepository()
        viewModel = AuthViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun login_withCorrectCredentials_shouldSucceed() = runTest {
        val testUser = User(name = "test", password = "1234", age = 25)
        fakeRepository.insertUser(testUser)
        var loginSuccess = false

        viewModel.login("test", "1234") { success, _ ->
            loginSuccess = success
        }

        assertTrue(loginSuccess)
        assertNotNull(viewModel.currentUser.value)
    }

    @Test
    fun login_withIncorrectCredentials_shouldFail() = runTest {
        val testUser = User(name = "test", password = "1234", age = 25)
        fakeRepository.insertUser(testUser)
        var loginSuccess = true

        viewModel.login("test", "contraseña-incorrecta") { success, _ ->
            loginSuccess = success
        }

        assertFalse(loginSuccess)
        assertNull(viewModel.currentUser.value)
    }

    @Test
    fun register_withNewUser_shouldSucceed() = runTest {
        // Arrange
        var registerSuccess = false

        // Act
        viewModel.register("newUser", "password") { success, _ ->
            registerSuccess = success
        }

        // Assert
        assertTrue("El registro debería ser exitoso", registerSuccess)
        assertEquals("El usuario debería haber sido añadido al repositorio", 1, fakeRepository.countUsers())
    }

    @Test
    fun register_withExistingUser_shouldFail() = runTest {
        // Arrange
        val existingUser = User(name = "existingUser", password = "1234", age = 30)
        fakeRepository.insertUser(existingUser)
        var registerSuccess = true

        // Act
        viewModel.register("existingUser", "newPassword") { success, _ ->
            registerSuccess = success
        }

        // Assert
        assertFalse("El registro debería fallar para un usuario existente", registerSuccess)
        assertEquals("No se debería añadir un nuevo usuario", 1, fakeRepository.countUsers())
    }
}
