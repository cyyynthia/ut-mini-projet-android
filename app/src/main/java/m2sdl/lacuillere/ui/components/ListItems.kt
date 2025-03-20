package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Reservation
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.Review
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.ui.theme.DrawGreen
import m2sdl.lacuillere.ui.theme.DrawRed
import m2sdl.lacuillere.ui.theme.DrawYellow
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ListItemDataRow(icon: ImageVector, data: String) {
	Row(
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(
			icon,
			contentDescription = null,
			modifier = Modifier.size(MaterialTheme.typography.bodyMedium.fontSize.value.dp),
		)
		Text(data, style = MaterialTheme.typography.bodyMedium)
	}
}

@Composable
fun StarRatingIcon(rating: Double, current: Int) {
	Icon(
		when {
			rating >= current + 1 -> Icons.Filled.Star
			rating >= current + 0.5 -> Icons.AutoMirrored.Filled.StarHalf
			else -> Icons.Filled.StarBorder
		},
		contentDescription = null,
		modifier = Modifier.size(MaterialTheme.typography.bodyMedium.fontSize.value.dp),
		tint = DrawYellow,
	)
}

@Composable
fun StarRating(rating: Double) {
	Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(bottom = 2.dp)) {
		StarRatingIcon(rating, 0)
		StarRatingIcon(rating, 1)
		StarRatingIcon(rating, 2)
		StarRatingIcon(rating, 3)
		StarRatingIcon(rating, 4)
	}
}

@Composable
fun RestoProfilePicture(restaurant: Restaurant) {
	Column(modifier = Modifier.padding(top = 4.dp)) {
		restaurant.banner.HackyImage(
			fitIn = DpSize(64.dp, 64.dp),
			contentDescription = null,
			modifier = Modifier
				.size(64.dp, 64.dp)
				.clip(RoundedCornerShape(8.dp))
		)
	}
}

@Composable
fun ReviewBody(name: String, review: Review) {
	Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
		Column {
			Text(name, style = MaterialTheme.typography.titleLarge)

			Row(
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(review.date))
				Text("•")
				StarRating(review.note.toDouble())
			}
		}

		Text(review.text)
	}
}

@Composable
fun RestoListItem(
	restaurant: Restaurant,
	onClick: () -> Unit,
	contentPadding: PaddingValues = PaddingValues(),
	modifier: Modifier = Modifier,
) {
	val dtf = DateTimeFormatter.ofPattern("HH:mm")
	val restaurantRating = remember {
		RepositoryLocator.getReviewRepository()
			.filterBy { it.restaurantId == restaurant.id }
			.map { it.note }
			.average()
	}

	Row(
		horizontalArrangement = Arrangement.spacedBy(24.dp),
		modifier = modifier
			.clickable { onClick() }
			.padding(contentPadding)
	) {
		RestoProfilePicture(restaurant)

		Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
			Column {
				Text(restaurant.name, style = MaterialTheme.typography.titleLarge)

				Row(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					if (restaurant.isCurrentlyOpen()) {
						val closingHours = restaurant.nextClosingHours()
						Box(
							modifier = Modifier
								.size(8.dp)
								.clip(RoundedCornerShape(4.dp))
								.background(DrawGreen)
						)
						Text("Ouvert – ferme à ${dtf.format(closingHours)}")
					} else {
						val openingHours = restaurant.nextOpeningHours()
						Box(
							modifier = Modifier
								.size(8.dp)
								.clip(RoundedCornerShape(4.dp))
								.background(DrawRed)
						)
						Text("Fermé – ouvre à ${dtf.format(openingHours)}")
					}
				}

				StarRating(restaurantRating)
			}

			ListItemDataRow(Icons.Default.LocationOn, restaurant.addressShort)
			ListItemDataRow(Icons.Default.Phone, restaurant.telephone)
		}
	}
}

@Composable
fun ReviewListItem(review: Review, modifier: Modifier = Modifier) {
	val user by derivedStateOf {
		RepositoryLocator.getUserRepository().findById(review.userId)
			?: throw RuntimeException("Bad record?!") // Should be properly handled, but meh.
	}

	Row(
		horizontalArrangement = Arrangement.spacedBy(24.dp),
		modifier = modifier
	) {
		Column(modifier = Modifier.padding(top = 4.dp)) {
			user.avatar.HackyImage(
				fitIn = DpSize(64.dp, 64.dp),
				contentDescription = null,
				modifier = Modifier
					.size(64.dp, 64.dp)
					.clip(RoundedCornerShape(32.dp))
			)
		}

		ReviewBody(user.name, review)
	}
}

@Composable
fun ReviewListHistoryItem(review: Review, modifier: Modifier = Modifier) {
	val restaurant by derivedStateOf {
		RepositoryLocator.getRestaurantRepository().findById(review.restaurantId)
			?: throw RuntimeException("Bad record?!") // Should be properly handled, but meh.
	}

	Row(
		horizontalArrangement = Arrangement.spacedBy(24.dp),
		modifier = modifier
	) {
		RestoProfilePicture(restaurant)
		ReviewBody(restaurant.name, review)
	}
}

@Composable
fun ReservationListItem(reservation: Reservation, modifier: Modifier = Modifier) {
	val restaurant by derivedStateOf {
		RepositoryLocator.getRestaurantRepository().findById(reservation.restaurantId)
			?: throw RuntimeException("Bad record?!") // Should be properly handled, but meh.
	}

	Row(
		horizontalArrangement = Arrangement.spacedBy(24.dp),
		modifier = modifier
	) {
		RestoProfilePicture(restaurant)
		Column {
			Text(restaurant.name, style = MaterialTheme.typography.titleLarge)
			Text(SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(reservation.date))
			Text("Pour ${reservation.peopleCount} personne${if (reservation.peopleCount != 1) "s" else ""}")
			Text("Au nom de: ${reservation.name}")
		}
	}
}
