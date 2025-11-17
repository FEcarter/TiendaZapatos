package com.example.tiendazapatos

import com.example.tiendazapatos.ui.viewmodel.PostViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlinx.coroutines.test.runTest


@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest {

    @Test
    fun `cuando se inicializa el ViewModel carga los posts correctamente`() = runTest {
        // Arrange
        val fakeRepository = FakePostRepository()
        val viewModel = PostViewModel(fakeRepository)

        // Act
        val posts = viewModel.postList.first()

        // Assert
        assertEquals(2, posts.size)
        assertEquals("Titulo de prueba 1", posts.first().title)
    }
}