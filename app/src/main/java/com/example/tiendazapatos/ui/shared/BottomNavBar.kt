package com.example.tiendazapatos.ui.shared

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Inicio : BottomNavItem("inicio", "Inicio", Icons.Filled.Home)
    object Producto : BottomNavItem("producto", "Producto", Icons.Filled.ShoppingCart)
    object Soporte : BottomNavItem("soporte", "Soporte", Icons.Filled.SupportAgent)
}

@Composable
fun AppBottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Inicio,
        BottomNavItem.Producto,
        BottomNavItem.Soporte
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            val iconScale by animateFloatAsState(
                targetValue = if (isSelected) 1.2f else if (isHovered) 1.1f else 1.0f,
                animationSpec = tween(durationMillis = 200),
                label = "iconScale"
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                animationSpec = tween(durationMillis = 200),
                label = "textColor"
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.scale(iconScale)
                    )
                },
                label = { Text(item.label, color = textColor) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                ),
                interactionSource = interactionSource
            )
        }
    }
}
