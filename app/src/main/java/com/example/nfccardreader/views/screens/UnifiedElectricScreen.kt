import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.nfccardreader.data.entities.CardType

@Composable
fun UnifiedElectricScreen(nfcViewModel: NfcViewModel) {
    val cardResult = nfcViewModel.cardResults

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Unified Electric Screen", style = MaterialTheme.typography.h5)
        for (result in cardResult) {
            if (result.type == CardType.UNIFIED_ELECTRIC) {
                Text(text = "Detected: ${result.detected}")
                Text(text = "Serial: ${result.serial}")
                Text(text = "Type: ${result.type}")
                Text(text = "Card ID: ${result.buffer.split(" ").subList(0,10)}")
            }
        }
    }
}