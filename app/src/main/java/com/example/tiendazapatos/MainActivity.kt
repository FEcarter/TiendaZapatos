package com.example.tiendazapatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendazapatos.data.remote.AppDatabase
import com.example.tiendazapatos.ui.screen.UserScreen
import com.example.tiendazapatos.ui.theme.TiendaZapatosTheme
import com.example.tiendazapatos.viewmodel.UserViewModel
import com.example.tiendazapatos.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.o
        nCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(applicationContext).userDao()
        val factory = UserViewModelFactory(dao)

        enableEdgeToEdge()
        setContent {
            val viewModel: UserViewModel = viewModel(factory = factory)
            TiendaZapatosTheme {
                UserScreen(viewModel)
            }
        }
    }
}
