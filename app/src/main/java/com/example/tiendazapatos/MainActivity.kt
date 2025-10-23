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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiendazapatos.data.remote.AppDatabase
import com.example.tiendazapatos.ui.screen.*
import com.example.tiendazapatos.ui.shared.AppBottomNavBar
import com.example.tiendazapatos.ui.shared.AppTopAppBar
import com.example.tiendazapatos.ui.theme.TiendaZapatosTheme
import com.example.tiendazapatos.ui.viewmodel.AuthViewModel
import com.example.tiendazapatos.ui.viewmodel.ProductViewModel
import com.example.tiendazapatos.viewmodel.AuthViewModelFactory
import com.example.tiendazapatos.viewmodel.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiendaZapatosTheme {
                val database = AppDatabase.getDatabase(applicationContext)
                val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(database.userDao()))
                val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(database.orderDao()))

                val navController = rememberNavController()
                val cartItems by productViewModel.cartItems.collectAsState()
                val currentUser by authViewModel.currentUser.collectAsState()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        if (currentRoute != "splash_screen") {
                            AppTopAppBar(
                                navController = navController,
                                cartItemCount = cartItems.size,
                                userName = currentUser?.name,
                                onLogout = { authViewModel.logout() }
                            )
                        }
                    },
                    bottomBar = {
                        if (currentRoute != "splash_screen") {
                            AppBottomNavBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "splash_screen", modifier = Modifier.padding(innerPadding)) {
                        composable("splash_screen") { SplashScreen(navController = navController) }
                        composable("inicio") { HomeScreen(navController = navController) }
                        composable("cliente") { UserScreen() }
                        composable("producto") { ProductScreen(productViewModel = productViewModel, navController = navController) }
                        composable("cart") { CartScreen(productViewModel = productViewModel) }
                        composable("admin") { AdminScreen(navController = navController, productViewModel = productViewModel) }
                        composable("addProduct") { AddProductScreen(navController = navController, productViewModel = productViewModel) }
                        composable("orderHistory") { OrderHistoryScreen(navController = navController, productViewModel = productViewModel) }
                        composable(
                            route = "editProduct/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("productId")
                            if (productId != null) {
                                EditProductScreen(navController, productViewModel, productId)
                            }
                        }
                        composable(
                            route = "productDetail/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("productId")
                            if (productId != null) {
                                ProductDetailScreen(navController, productViewModel, productId)
                            }
                        }
                        composable("login") { LoginScreen(navController = navController, authViewModel = authViewModel) }
                        composable("register") { RegisterScreen(navController = navController, authViewModel = authViewModel) }
                    }
                }
            }
        }
    }
}
