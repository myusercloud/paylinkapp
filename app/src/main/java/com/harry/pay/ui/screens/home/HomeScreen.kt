package com.harry.pay.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController){


}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(navController = rememberNavController())
}