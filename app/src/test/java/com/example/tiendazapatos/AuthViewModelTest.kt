package com.example.tiendazapatos

import com.example.tiendazapatos.data.model.AuthRequest
import com.example.tiendazapatos.data.remote.model.User
import com.example.tiendazapatos.data.repository.UserRepositoryInterface
import com.example.tiendazapatos.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

// CORRECCIÓN FINAL Y DEFINITIVA: El repositorio falso ahora implementa la INTERFAZ.
class FakeUserRepository : UserRepositoryInterface {
    private val users = mutableMapOf<String, String>()

    // Se añade la palabra clave `override` que faltaba
    override suspend fun login(authRequest: AuthRequest): Response<Unit> {
        val storedPassword = users[authRequest.username]
        return if (storedPassword != null && storedPassword == authRequest.password) {
            Response.success(Unit)
        } else {
            Response.error(401, "Unauthorized".toResponseBody(null))
        }
    }

    // Se añade la palabra clave `override` que faltaba
    override suspend fun register(authRequest: AuthRequest): Response<Unit> {
        if (users.containsKey(authRequest.username)) {
            return Response.error(409, "Conflict".toResponseBody(null))
        }
        users[authRequest.username] = authRequest.password
        return Response.success(201, Unit)
    }
}

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AuthViewModel
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
        fakeRepository.register(AuthRequest("test", "1234"))
        var loginSuccess = false
        viewModel.login("test", "1234") { success, _ -> loginSuccess = success }
        assertTrue(loginSuccess)
        assertNotNull(viewModel.currentUser.value)
    }

    @Test
    fun login_withIncorrectCredentials_shouldFail() = runTest {
        fakeRepository.register(AuthRequest("test", "1234"))
        var loginSuccess = true
        viewModel.login("test", "wrong_password") { success, _ -> loginSuccess = success }
        assertFalse(loginSuccess)
        assertNull(viewModel.currentUser.value)
    }

    @Test
    fun register_withNewUser_shouldSucceed() = runTest {
        var registerSuccess = false
        viewModel.register("newUser", "password") { success, _ -> registerSuccess = success }
        assertTrue(registerSuccess)
    }

    @Test
    fun register_withExistingUser_shouldFail() = runTest {
        fakeRepository.register(AuthRequest("existingUser", "1234"))
        var registerSuccess = true
        viewModel.register("existingUser", "newPassword") { success, _ -> registerSuccess = success }
        assertFalse(registerSuccess)
    }

    @Test
    fun logout_shouldClearCurrentUser() = runTest {
        fakeRepository.register(AuthRequest("test", "1234"))
        viewModel.login("test", "1234") { _, _ -> }
        assertNotNull(viewModel.currentUser.value)
        viewModel.logout()
        assertNull(viewModel.currentUser.value)
    }
}
