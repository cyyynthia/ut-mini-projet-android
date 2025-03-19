package m2sdl.lacuillere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.ui.screens.Book
import m2sdl.lacuillere.ui.screens.Home
import m2sdl.lacuillere.ui.screens.ReservationHistory
import m2sdl.lacuillere.ui.screens.ReservationHistoryScreen
import m2sdl.lacuillere.ui.screens.Resto
import m2sdl.lacuillere.ui.screens.RestoBookScreen
import m2sdl.lacuillere.ui.screens.ReviewHistory
import m2sdl.lacuillere.ui.screens.ReviewHistoryScreen
import m2sdl.lacuillere.ui.screens.home.HomeScreen
import m2sdl.lacuillere.ui.screens.resto.RestoScreen
import m2sdl.lacuillere.ui.theme.LaCuillereTheme
import m2sdl.lacuillere.viewmodel.MapViewModel

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		RepositoryLocator.init(savedInstanceState)

		setContent {
			val navController = rememberNavController()
			val mapViewModel: MapViewModel by viewModels()

			val restaurants = RepositoryLocator.getRestaurantRepository().findAll()

			LaCuillereTheme {
				Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
					NavHost(navController = navController, startDestination = Home) {
						composable<Home> {
							HomeScreen(
								model = mapViewModel,
								restaurants = restaurants,
								onNavigateReservationHistory = { navController.navigate(ReservationHistory) },
								onNavigateReviewHistory = { navController.navigate(ReviewHistory) },
								onNavigateToRestaurant = { navController.navigate(Resto(it.id)) },
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
									onNavigateToBook = { navController.navigate(Book(resto.uuid)) })
							} ?: navController.navigate(Home)
						}

						// I'd love to use a different animation for this route, but it's a pita to get right
						// And I'm out of time :)
						composable<Book> { backStackEntry ->
							val book: Book = backStackEntry.toRoute()
							val restaurant = RepositoryLocator.getRestaurantRepository().findById(book.uuid)
							restaurant?.let { RestoBookScreen(restaurant, onBack = { navController.popBackStack() } ) } ?: navController.navigate(Home)
						}

						composable<ReservationHistory> {
							ReservationHistoryScreen(onBack = { navController.popBackStack() })
						}

						composable<ReviewHistory> {
							ReviewHistoryScreen(onBack = { navController.popBackStack() })
						}
					}
				}
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle) {
		RepositoryLocator.save(outState) // lmao²
		super.onSaveInstanceState(outState)
	}
}
