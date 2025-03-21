package m2sdl.lacuillere.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.ui.components.BackButton
import m2sdl.lacuillere.ui.components.ReviewListHistoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewHistoryScreen(targetRestaurant: Restaurant? = null, onBack: () -> Unit) {
	val myself = remember { RepositoryLocator.getUserRepository().findMyself() }
	val reviews = remember {
		if (targetRestaurant != null)
			RepositoryLocator.getReviewRepository().filterByUserAndRestaurant(myself, targetRestaurant)
		else
			RepositoryLocator.getReviewRepository().filterByUser(myself)
	}

	Scaffold(
		topBar = {
			TopAppBar(
				navigationIcon = { BackButton(onBack = onBack, contentDescription = "Retour") },
				title = { Text("Mes avis") }
			)
		}
	) { innerPadding ->
		if (reviews.isEmpty()) {
			Text("no reviews")
		} else {
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
					.padding(start = 16.dp, end = 16.dp, top = 8.dp),
			) {
				itemsIndexed(reviews) { index, review ->
					ReviewListHistoryItem(review)

					if (index < reviews.lastIndex)
						HorizontalDivider(Modifier.padding(horizontal = 16.dp))
				}
			}
		}
	}
}
