package com.example.tiendazapatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendazapatos.ui.screen.HomeScreen
import com.example.tiendazapatos.ui.screen.ProductScreen
import com.example.tiendazapatos.ui.screen.UserScreen
import com.example.tiendazapatos.ui.shared.MyFooter
import com.example.tiendazapatos.ui.shared.MyTopAppBar
import com.example.tiendazapatos.ui.theme.TiendaZapatosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiendaZapatosTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { MyTopAppBar(navController) },
                    bottomBar = { MyFooter() }
                ) { innerPadding ->
                    NavHost(navController = navController, startDestination = "inicio", modifier = Modifier.padding(innerPadding)) {
                        composable("inicio") { HomeScreen() }
                        composable("cliente") { UserScreen() }
                        composable("producto") { ProductScreen() }
                    }
                }
            }
        }
    }
}
