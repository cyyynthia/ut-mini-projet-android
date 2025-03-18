package m2sdl.lacuillere.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import m2sdl.lacuillere.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationHistoryScreen(onBack: () -> Unit) {
	Scaffold(
		topBar = {
			TopAppBar(
				navigationIcon = { BackButton(onBack = onBack, contentDescription = "Retour") },
				title = { Text("Mes réservations") }
			)
		}
	) {

	}
}
