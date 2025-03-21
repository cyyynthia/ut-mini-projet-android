package m2sdl.lacuillere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.ui.screens.Book
import m2sdl.lacuillere.ui.screens.Home
import m2sdl.lacuillere.ui.screens.ReservationHistory
import m2sdl.lacuillere.ui.screens.ReservationHistoryScreen
import m2sdl.lacuillere.ui.screens.Resto
import m2sdl.lacuillere.ui.screens.RestoBookScreen
import m2sdl.lacuillere.ui.screens.RestoReviewScreen
import m2sdl.lacuillere.ui.screens.ReviewHistory
import m2sdl.lacuillere.ui.screens.ReviewHistoryScreen
import m2sdl.lacuillere.ui.screens.SubmitReview
import m2sdl.lacuillere.ui.screens.home.HomeScreen
import m2sdl.lacuillere.ui.screens.resto.RestoScreen
import m2sdl.lacuillere.ui.theme.LaCuillereTheme
import m2sdl.lacuillere.viewmodel.MapViewModel

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		// lmao
		RepositoryLocator.init(this)

		setContent {
			val navController = rememberNavController()
			val mapViewModel: MapViewModel by viewModels()

			val restaurants = RepositoryLocator.getRestaurantRepository().findAll()

			LaCuillereTheme {
				NavHost(modifier = Modifier.fillMaxSize(), navController = navController, startDestination = Home) {
					composable<Home> {
						HomeScreen(
							model = mapViewModel,
							restaurants = restaurants,
							onNavigateReservationHistory = { navController.navigate(ReservationHistory) },
							onNavigateReviewHistory = { navController.navigate(ReviewHistory()) },
							onNavigateToRestaurant = { navController.navigate(Resto(it.id)) },
							onNavigateToReviewsOf = { navController.navigate(ReviewHistory(it.id)) },
						)
					}

					// I'd love to use a different animation for this route, but it's a pita to get right
					// And I'm out of time :)
					composable<Resto> { backStackEntry ->
						val resto: Resto = backStackEntry.toRoute()
						val restaurant = RepositoryLocator.getRestaurantRepository().findById(resto.uuid)
						restaurant?.let {
							RestoScreen(
								restaurant = it,
								onNavigateToBook = { navController.navigate(Book(resto.uuid)) },
								onNavigateToSubmitReview = { navController.navigate(SubmitReview(resto.uuid)) },
								onBack = { navController.popBackStack() },
							)
						} ?: navController.navigate(Home)
					}

					// I'd love to use a different animation for this route, but it's a pita to get right
					// And I'm out of time :)
					composable<Book> { backStackEntry ->
						val book: Book = backStackEntry.toRoute()
						val restaurant = RepositoryLocator.getRestaurantRepository().findById(book.uuid)
						restaurant?.let { RestoBookScreen(restaurant, onBack = { navController.popBackStack() }) }
							?: navController.navigate(Home)
					}

					// I'd love to use a different animation for this route, but it's a pita to get right
					// And I'm out of time :)
					composable<SubmitReview> { backStackEntry ->
						val review: SubmitReview = backStackEntry.toRoute()
						val restaurant = RepositoryLocator.getRestaurantRepository().findById(review.uuid)
						restaurant?.let { RestoReviewScreen(restaurant, onBack = { navController.popBackStack() }) }
							?: navController.navigate(Home)
					}

					composable<ReservationHistory> {
						ReservationHistoryScreen(onBack = { navController.popBackStack() })
					}

					composable<ReviewHistory> { backStackEntry ->
						val review: ReviewHistory = backStackEntry.toRoute()
						val restaurant = review.uuid?.let { RepositoryLocator.getRestaurantRepository().findById(it) }
						ReviewHistoryScreen(
							targetRestaurant = restaurant,
							onBack = { navController.popBackStack() }
						)
					}
				}
			}
		}
	}

	override fun onPause() {
		println("Saving repo")
		RepositoryLocator.save(this) // lmao²
		super.onPause()
	}
}
