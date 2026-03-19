package com.example.myapplication

import androidx.compose.ui.window.ComposeUIViewController
import com.example.myapplication.database.DriverFactory

fun MainViewController() = ComposeUIViewController {
    val driver = DriverFactory().createDriver();
    App(driver)
}