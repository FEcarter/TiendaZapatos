package com.example.tiendazapatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendazapatos.ui.screen.AddProductScreen
import com.example.tiendazapatos.ui.screen.AdminScreen
import com.example.tiendazapatos.ui.screen.CartScreen
import com.example.tiendazapatos.ui.screen.HomeScreen
import com.example.tiendazapatos.ui.screen.ProductScreen
import com.example.tiendazapatos.ui.screen.UserScreen
import com.example.tiendazapatos.ui.shared.MyFooter
import com.example.tiendazapatos.ui.shared.MyTopAppBar
import com.example.tiendazapatos.ui.theme.TiendaZapatosTheme
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiendaZapatosTheme {
                val navController = rememberNavController()
                val productViewModel: ProductViewModel = viewModel()
                val cartItems by productViewModel.cartItems.collectAsState()

                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            navController = navController,
                            cartItemCount = cartItems.size
                        )
                    },
                    bottomBar = { MyFooter() }
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "inicio", modifier = Modifier.padding(innerPadding)) {
                        composable("inicio") { HomeScreen() }
                        composable("cliente") { UserScreen() }
                        composable("producto") { ProductScreen(productViewModel = productViewModel) }
                        composable("cart") { CartScreen(productViewModel = productViewModel) }
                        composable("admin") { AdminScreen(navController = navController, productViewModel = productViewModel) }
                        composable("addProduct") { AddProductScreen(navController = navController, productViewModel = productViewModel) }
                    }
                }
            }
        }
    }
}
