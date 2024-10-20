package com.example.nfccardreader
import NewWaterScreen
import NfcViewModel
import UnifiedElectricScreen
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nfccardreader.helper_classes.CardReaderHelper
import com.example.nfccardreader.data.entities.CardResult
import com.example.nfccardreader.data.entities.CardType
import com.example.nfccardreader.views.screens.OldElectricScreen
import com.example.nfccardreader.views.screens.GasScreen
import com.example.nfccardreader.views.screens.HomeScreen
import com.example.nfccardreader.views.screens.OldWaterScreen

class MainActivity : ComponentActivity() {
    var nfcAdapter: NfcAdapter? = null
    private val nfcViewModel: NfcViewModel by viewModels()
    val cardReaderHelper = CardReaderHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize NFC Adapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_LONG).show()
        } else if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC is disabled", Toast.LENGTH_LONG).show()
        }

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "home_screen"
            ) {
                composable("home_screen") { HomeScreen(nfcViewModel,navController) }
                composable("gas_screen") { GasScreen(nfcViewModel) }
                composable("old_electric") { OldElectricScreen(nfcViewModel) }
                composable("unified_electric") { UnifiedElectricScreen(nfcViewModel) }
                composable("new_water") { NewWaterScreen(nfcViewModel) }
                composable("old_water") { OldWaterScreen(nfcViewModel) }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        val intent = intent
        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                val isoDep = IsoDep.get(tag)
                readCard(isoDep)
            }
        }
    }

    private fun readCard(isoDep: IsoDep) {
        try {
            isoDep.connect()
            val newWaterCardResult = cardReaderHelper.newWaterCardFunCall(isoDep)
            val oldWaterCardResult = cardReaderHelper.oldWaterCardFunCall(isoDep)
            val unifiedElecCardResult = cardReaderHelper.unifiedElecCardFunCall(isoDep)
            val oldElecCardResult = cardReaderHelper.oldElecCardFunCall(isoDep)
            var cardDataToShow = CardResult(false, "Not Detected", CardType.NOT_SPECIFIED,"No Buffer")

            if(newWaterCardResult.detected){
                nfcViewModel.addCardResult(newWaterCardResult)
            }
            if(oldWaterCardResult.detected){
                nfcViewModel.addCardResult(oldWaterCardResult)
            }
            if(unifiedElecCardResult.detected){
                nfcViewModel.addCardResult(unifiedElecCardResult)
            }
            if(oldElecCardResult.detected){
                nfcViewModel.addCardResult(oldElecCardResult)
            }


            isoDep.close()

            // Update the ViewModel with the detected card result
//            nfcViewModel.updateCardResult(cardDataToShow)

        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Error reading NFC tag", Toast.LENGTH_LONG).show()
            }
        }
    }
}
