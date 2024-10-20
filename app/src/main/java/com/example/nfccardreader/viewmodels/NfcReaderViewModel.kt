import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.nfccardreader.data.entities.CardResult

class NfcViewModel : ViewModel() {
    var cardResults by mutableStateOf<List<CardResult>>(emptyList())
        private set

    fun addCardResult(cardResult: CardResult) {
        cardResults = cardResults + cardResult
    }


    fun clearCardResults() {
        cardResults = emptyList()
    }
}
