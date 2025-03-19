package m2sdl.lacuillere.ui.screens.resto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant

@Composable
fun AboutTab(restaurant: Restaurant, onNavigateToBook: () -> Unit) {
	Scaffold(
		floatingActionButton = {
			ExtendedFloatingActionButton(
				onClick = onNavigateToBook,
				icon = { Icon(Icons.Filled.CalendarMonth, null) },
				text = { Text(text = "Réserver") },
			)
		}
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.verticalScroll(rememberScrollState())
		) {
			Spacer(modifier = Modifier.height(8.dp))
			Text(text = "Adresse: ${restaurant.addressShort}", style = MaterialTheme.typography.bodyLarge)
			Spacer(modifier = Modifier.height(8.dp))
			Text(text = "Téléphone: ${restaurant.telephone}", style = MaterialTheme.typography.bodyLarge)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = "Horaires d'ouverture aujourd'hui: ${restaurant.openingHours.first().first} - ${restaurant.openingHours.first().second} ",
				style = MaterialTheme.typography.bodyLarge
			)
			Spacer(modifier = Modifier.height(8.dp))

			//ajouter Description
			//Gallerie
			//Menu
			//Ouverture détaillée + on click proposer réservation
		}
	}
}
