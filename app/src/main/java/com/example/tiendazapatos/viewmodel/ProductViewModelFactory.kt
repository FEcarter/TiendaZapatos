package com.example.tiendazapatos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendazapatos.data.remote.dao.OrderDao
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel

class ProductViewModelFactory(private val orderDao: OrderDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(orderDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
