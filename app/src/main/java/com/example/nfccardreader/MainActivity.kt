package com.example.nfccardreader

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfccardreader.ui.theme.NfcCardReaderTheme

class MainActivity : ComponentActivity() {
    var nfcAdapter: NfcAdapter? = null
    val cardReaderHelper = CardReaderHelper()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            //Setting up NFC
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            if (nfcAdapter == null) {
                Toast.makeText(this, "No NFC", Toast.LENGTH_LONG).show()
            }
            if (nfcAdapter?.isEnabled == false) {
                Toast.makeText(this, "NFC is disabled", Toast.LENGTH_LONG).show()
            }


            Scaffold { padding ->
                Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                    Text("Please touch a card")
                }
            }
        }
    }


    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onResume() {
        super.onResume()
        val intent = intent
        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            Log.d("Intent", intent.toString())
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                val isoDep = IsoDep.get(tag)
                readCard(isoDep)
            }
        }
    }

    fun readCard(isoDep: IsoDep): CardResult {
        var cardDataToShow = CardResult(false, "Not Detected", CardType.NOT_SPECIFIED)
        try {
            isoDep.connect()
            val newWaterCardResult = cardReaderHelper.newWaterCardFunCall(isoDep)
            val oldWaterCardResult = cardReaderHelper.oldWaterCardFunCall(isoDep)
            val unifiedElecCardResult = cardReaderHelper.unifiedElecCardFunCall(isoDep)
            val oldElecCardResult = cardReaderHelper.oldElecCardFunCall(isoDep)
            if (newWaterCardResult.type == CardType.NEW_WATER) {
                cardDataToShow = newWaterCardResult
            }
            if (oldWaterCardResult.type == CardType.OLD_WATER) {
                cardDataToShow = oldWaterCardResult
            }
            if (unifiedElecCardResult.type == CardType.UNIFIED_ELECTRIC) {
                cardDataToShow = unifiedElecCardResult
            }
            if (oldElecCardResult.type == CardType.OLD_ELECTRIC) {
                cardDataToShow = oldElecCardResult
            }

            isoDep.close()
            Log.d("Card Data", cardDataToShow.type.toString())
            runOnUiThread {
                Toast.makeText(this, "Card Data: ${cardDataToShow.type}", Toast.LENGTH_LONG).show()
                // Update cardResult state
                setContent {
                    var cardResult by remember { mutableStateOf(cardDataToShow) }

                    Scaffold { padding ->
                        Column(modifier = Modifier.padding(padding)) {
                            Text(
                                text = "Detected: ${cardResult?.detected}",
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                text = "Serial: ${cardResult?.serial}",
                                modifier = Modifier.padding(16.dp)
                            )
                            Text(
                                text = "Type: ${cardResult?.type}",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
            return cardDataToShow
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Error reading NFC tag", Toast.LENGTH_LONG).show()
            }
            return cardDataToShow
        }
    }

}

