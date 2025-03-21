package m2sdl.lacuillere.ui.screens.resto

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoScreen(
	restaurant: Restaurant,
	onNavigateToBook: () -> Unit,
	onNavigateToSubmitReview: () -> Unit,
	onBack: () -> Unit,
) {
	var selectedTabIndex by remember { mutableStateOf(0) }
	val tabs = listOf("À propos", "Avis")

	Scaffold(
		topBar = {
			MediumTopAppBar(
				navigationIcon = { BackButton(onBack = onBack, contentDescription = "Retour") },
				title = { Text(restaurant.name) },
			)
		}
	) { innerPadding ->
		Column(Modifier.padding(innerPadding)) {
			PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
				tabs.forEachIndexed { index, title ->
					Tab(
						selected = selectedTabIndex == index,
						onClick = { selectedTabIndex = index },
						text = { Text(title) }
					)
				}
			}

			when (selectedTabIndex) {
				0 -> AboutTab(restaurant, onNavigateToBook)
				1 -> ReviewsTab(restaurant, onNavigateToSubmitReview)
			}
		}
	}
}
