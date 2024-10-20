package com.example.nfccardreader.views.screens

import NfcViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nfccardreader.data.entities.CardType

@Composable
fun HomeScreen(nfcViewModel: NfcViewModel, navController: NavHostController) {
    val cardResult = nfcViewModel.cardResults
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to",
            style = MaterialTheme.typography.h4.copy(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = "Utilities Cards Reader",
            style = MaterialTheme.typography.h4.copy(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        for (result in cardResult) {
            if (result.type == CardType.NEW_WATER) {
                Button(
                    onClick = { navController.navigate("new_water") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "New Water", color = Color.White)
                }
            }
            if (result.type == CardType.OLD_WATER) {
                Button(
                    onClick = { navController.navigate("old_water") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "Old Water", color = Color.White)
                }
            }
            if (result.type == CardType.UNIFIED_ELECTRIC) {
                Button(
                    onClick = { navController.navigate("unified_electric") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "Unified Electric", color = Color.White)
                }
            }
            if (result.type == CardType.OLD_ELECTRIC) {
                Button(
                    onClick = { navController.navigate("old_electric") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "Old Electric", color = Color.White)

                }
            }
            if (result.type == CardType.GAS) {
                Button(
                    onClick = { navController.navigate("gas_screen") },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "Gas", color = Color.White)

                }
            }
        }
    }
}
