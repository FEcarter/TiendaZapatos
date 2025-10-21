package com.example.tiendazapatos.ui.shared

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MyFooter() {
    BottomAppBar {
        Text("© 2024 Tienda de Zapatos")
    }
}

@Preview
@Composable
fun PreviewMyFooter() {
    MyFooter()
}
