package m2sdl.lacuillere.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage

@Composable
fun RestoScreen(restaurant: Restaurant) {
	var selectedTabIndex by remember { mutableStateOf(0) }
	var showDialog by remember { mutableStateOf(false) }
	val tabs = listOf("A propos", "Avis")

	Column(modifier = Modifier.fillMaxSize()) {
		Box{
			AsyncImage(
				model = "https://pbs.twimg.com/media/FgagCUvWAAIhOgR.jpg",
				contentDescription = "Image du restaurant",
				modifier = Modifier
					.fillMaxWidth()
					.height(200.dp)
					.clickable { showDialog = true },
				contentScale = ContentScale.Crop
			)
		}
		Text(
			text = restaurant.name,
			style = MaterialTheme.typography.headlineLarge,
			modifier = Modifier.padding(16.dp)
		)
		TabRow(selectedTabIndex = selectedTabIndex) {
			tabs.forEachIndexed { index, title ->
				Tab(
					selected = selectedTabIndex == index,
					onClick = { selectedTabIndex = index },
					text = { Text(title) }
				)
			}
		}
		when (selectedTabIndex) {
			0 -> AProposTab(restaurant)
			1 -> AvisTab(restaurant)
		}
	}
	if (showDialog) {
		Dialog(onDismissRequest = { showDialog = false }) {
			Box(modifier = Modifier.fillMaxSize()) {
				AsyncImage(
					model = "https://pbs.twimg.com/media/FgagCUvWAAIhOgR.jpg",
					contentDescription = "Image du restaurant",
					modifier = Modifier
						.fillMaxSize()
						.clickable { showDialog = false }
				)
			}
		}
	}
}

@Composable
fun AProposTab(restaurant: Restaurant) {
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

@Composable
fun AvisTab(restaurant: Restaurant) {

}