package m2sdl.lacuillere.ui.screens.resto

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.ui.components.EmptyScreenAction
import m2sdl.lacuillere.ui.components.ReviewListItem

@Composable
fun ReviewsTab(restaurant: Restaurant, onNavigateToSubmitReview: () -> Unit) {
	val reviews = remember { RepositoryLocator.getReviewRepository().filterByRestaurant(restaurant) }

	Scaffold(
		floatingActionButton = {
			if (reviews.isNotEmpty())
				FloatingActionButton(onClick = onNavigateToSubmitReview) {
					Icon(Icons.Filled.Reviews, null)
				}
		}
	) {
		if (reviews.isEmpty()) {
			EmptyScreenAction(
				icon = Icons.Filled.Reviews,
				text = "Personne n'a encore laissé d'avis",
				button = "Laisser un avis",
				onAction = onNavigateToSubmitReview
			)
		} else {
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(start = 16.dp, end = 16.dp, top = 8.dp),
			) {
				itemsIndexed(reviews) { index, review ->
					ReviewListItem(review)

					if (index < reviews.lastIndex)
						HorizontalDivider(Modifier.padding(horizontal = 16.dp))
				}
			}
		}
	}

}
